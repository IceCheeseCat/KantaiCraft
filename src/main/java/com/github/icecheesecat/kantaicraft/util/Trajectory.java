package com.github.icecheesecat.kantaicraft.util;

import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.trajectory.ClientSetDestroyTrajectoryPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.function.Predicate;

public class Trajectory implements INBTSerializable<CompoundTag> {

    int id;
    UUID gunner;
    Physics physics;
    int alive = MAX_ALIVE;
    static final int MAX_ALIVE = 400;
    AABB boundingBox;
    float projectileSize;
    float damage;
    boolean stopped = false;
    public static final float SMALL_PROJECTILE_SIZE = 0.1f;
    public static final float MEDIUM_PROJECTILE_SIZE = 0.2f;
    public static final float BIG_PROJECTILE_SIZE = 0.3f;
    public static final int DESTROYER_TIME_AFTER_HIT = 100;

    final @NotNull Predicate<Entity> entityPredicate;

    private Trajectory() {
        this.entityPredicate = (e) -> true;
    }

    public Trajectory(int id, UUID gunner, Vec3 pos, Vec3 vel, Vec3 acc, float projectile_size, float damage, @NotNull Predicate<Entity> entityPredicate) {
        this.id = id;
        this.gunner = gunner;
        this.physics = new Physics(pos, vel, acc);
        this.projectileSize = projectile_size;
        this.damage = damage;
        this.boundingBox = AABB.ofSize(pos, projectile_size, projectile_size, projectile_size);
        this.entityPredicate = entityPredicate;
    }

    public Trajectory(Trajectory t) {
        this.id = t.getId();
        this.gunner = t.gunner;
        this.physics = t.physics;
        this.projectileSize = t.projectileSize;
        this.damage = t.damage;
        this.boundingBox = t.boundingBox;
        this.alive = t.getAlive();
        this.stopped = t.stopped;
        this.entityPredicate = t.entityPredicate;
    }

    public Trajectory asCopy() {
        return new Trajectory(this);
    }

    public void doHitCheck(Level level) {
        if (isStopped()) return;

        TrajectoryHitResult hitResult = this.getHitResult(level);
        if (!hitResult.missed()) {
            stopPhysicAfterHit(hitResult.getLocation());
            this.stopped = true;
            if (hitResult.type == HitResult.Type.ENTITY) {
                doHitTarget((ServerLevel) level, hitResult.entity);
            }
            // sync trajectory has hit to client
            ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientSetDestroyTrajectoryPacket(this.id, hitResult.getLocation().toVector3f(), MAX_ALIVE - alive, this.physics));
            this.setAlive(DESTROYER_TIME_AFTER_HIT);
        }
    }

    public void stopPhysicAfterHit(Vec3 hitLocation) {
        this.physics.setNextPosition(hitLocation);
        this.physics.vel = Vec3.ZERO;
        this.physics.acc = Vec3.ZERO;
    }

    public void tick() {
        this.alive--;
        if (this.isStopped()) return;
        this.physics.update();
        // update bounding box position
        this.boundingBox = this.boundingBox.move(this.physics.displacement());
    }


    public boolean checkTimeout() {
        return alive <= 0;
    }


    private TrajectoryHitResult getHitResult(Level pLevel) {
        var entityHitResult = getEntityHitResult(pLevel, boundingBox);
        if (!entityHitResult.missed()) {
            return new TrajectoryHitResult(entityHitResult.getLocation(), entityHitResult.entity);
        }

        BlockHitResult blockHitResult = pLevel.clip(new ClipContext(physics.prevPos, physics.pos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        if (blockHitResult.getType() != HitResult.Type.MISS) {
            return new TrajectoryHitResult(blockHitResult.getLocation(), blockHitResult.getBlockPos());
        }

        return TrajectoryHitResult.MISS;
    }

    @NotNull
    private TrajectoryHitResult getEntityHitResult(Level pLevel, AABB trajectoryBox) {
//        double d0 = Double.MAX_VALUE;

        List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, trajectoryBox.inflate(5.0d), entityPredicate);
        if (entities.isEmpty()) return TrajectoryHitResult.MISS;

        List<Pair<Entity, Optional<Vec3>>> possibleHits = new ArrayList<>();
        for (var le: entities) {
            var optional = le.getBoundingBox().clip(physics.prevPos, physics.pos);
            if (optional.isPresent()) {
                possibleHits.add(new Pair<>(le, optional));
            }
        }

        if (!possibleHits.isEmpty()) {
            var closest = possibleHits.stream().min(Comparator.comparingDouble((a) -> a.getB().get().distanceTo(physics.prevPos)));
            return new TrajectoryHitResult(closest.get().getB().get(), closest.get().getA());
        }

        return TrajectoryHitResult.MISS;
    }

    public int getId() {
        return id;
    }

    public float getDamage() {
        return damage;
    }

    public Physics getPhysics() {
        return physics;
    }

    public void setPhysics(Physics physics) {
        this.physics = physics;
    }

    public double getProjectileSize() {
        return this.projectileSize;
    }

    public AABB getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(AABB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public UUID getGunner() {
        return gunner;
    }

    public void print() {
        System.out.println(this.id + ": ");
        System.out.print(this.physics.pos + ", ");
        System.out.print(this.physics.vel + ", ");
        System.out.println(this.physics.acc);
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public static Vec3 calFireVec(Vec3 a, Vec3 b, Vec3 g, double v) {
        double theta0, theta1;
        double h = b.y - a.y;
        double d = new Vec3(b.x - a.x, 0, b.z - a.z).length();

        double A = g.y*d*d/2/v/v;
        double B = d;

        double v0 = (-B + Math.sqrt(B*B - 4*A*(A-h))) /2/A;
        double v1 = (-B - Math.sqrt(B*B - 4*A*(A-h))) /2/A;

        theta0 = Math.atan(v0);
        theta1 = Math.atan(v1);

        double theta;
        if (theta0 < theta1) {
            theta = theta0;
        }
        else {
            theta = theta1;
        }

//        System.out.println("Theta: " + theta * 180 / Math.PI);
//
//        System.out.println("Theta0: " + theta0 * 180 / Math.PI);
//
//        System.out.println("Theta1: " + theta1 * 180 / Math.PI);
        if (Double.isNaN(theta)) {
            return null;
        }

        Vec3 flat = new Vec3(b.x - a.x, 0, b.z - a.z).normalize();
        Vec3 fireDirectionVec = flat.add(0, Math.tan(theta),0).normalize();

        return fireDirectionVec.scale(v);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("id", this.id);
        nbt.putUUID("gunner", this.gunner);
        nbt.put("physics", this.physics.serializeNBT());
        nbt.putInt("alive", this.alive);
        nbt.putFloat("projectile_size", this.projectileSize);
        nbt.putFloat("damage", this.damage);
        nbt.putBoolean("stopped", this.stopped);
        return nbt;
    }



    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.id = nbt.getInt("id");
        this.gunner = nbt.getUUID("gunner");
        this.physics = Physics.create(nbt.getCompound("physics"));
        this.alive = nbt.getInt("alive");
        this.projectileSize = nbt.getFloat("projectile_size");
        this.damage = nbt.getFloat("damage");
        this.boundingBox = AABB.ofSize(this.physics.pos, this.projectileSize, this.projectileSize, this.projectileSize);
        this.stopped = nbt.getBoolean("stopped");
    }

    public static Trajectory create(CompoundTag nbt) {
        Trajectory trajectory = new Trajectory();
        trajectory.deserializeNBT(nbt);
        return trajectory;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Trajectory t && t.id == this.id;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean b) {
        this.stopped = b;
    }


    private void doHitTarget(ServerLevel serverLevel, Entity entity) {
        Entity gunnerEntity = serverLevel.getEntity(gunner);
        if (gunnerEntity == null) {
            entity.hurt(serverLevel.damageSources().mobAttack(null), damage);
        }
        else {
            entity.hurt(gunnerEntity.damageSources().mobAttack((LivingEntity) gunnerEntity), damage);
        }
    }
}
