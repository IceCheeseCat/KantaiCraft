package com.github.icecheesecat.kantaicraft.util;

import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.ClientSetDestroyTrajectoryPacket;
import com.github.icecheesecat.kantaicraft.network.packet.TrajectoryPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
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
    @Nullable HitResult hitResult;
    public static final float SMALL_PROJECTILE_SIZE = 0.1f;
    public static final float MEDIUM_PROJECTILE_SIZE = 0.2f;
    public static final float BIG_PROJECTILE_SIZE = 0.3f;
    public static final int DESTROYER_TIME_AFTER_HIT = 100;

    final @Nullable Predicate<Entity> entityPredicate;

    private Trajectory() {
        this.entityPredicate = null;
    }

    public Trajectory(int id, UUID gunner, Vec3 pos, Vec3 vel, Vec3 acc, float projectile_size, float damage, Predicate<Entity> entityPredicate) {
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

    public void tick(Level level) {
        if (this.stopped) return;
        // server side only !!!
        if (!level.isClientSide && this.entityPredicate != null) {
            this.hitResult = this.getHitResult(level);
            if (this.hitResult != null && this.hitResult.getType() != HitResult.Type.MISS) {
                this.physics.setNextPosition(this.hitResult.getLocation());
                this.stopped = true;
                if (this.hitResult instanceof EntityHitResult entityHitResult) {
                    doHitTarget((ServerLevel) level);
                }
                // sync trajectory has hit to client
                ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientSetDestroyTrajectoryPacket(this.id, hitResult.getLocation().toVector3f(), MAX_ALIVE - alive));
                this.setAlive(DESTROYER_TIME_AFTER_HIT);
                return;
            }
        }

        this.physics.update();
        // update bounding box position
        this.boundingBox = this.boundingBox.move(this.physics.displacement());
        this.alive--;
//        this.print();
    }


    public boolean checkTimeout() {
        return alive <= 0;
    }


    private HitResult getHitResult(Level pLevel) {
        HitResult hitresult = pLevel.clip(new ClipContext(physics.prevPos, physics.pos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));

        HitResult hitresult1 = getEntityHitResult(pLevel);
        if (hitresult1 != null) {
            hitresult = hitresult1;
        }

        return hitresult;
    }

    @Nullable
    private EntityHitResult getEntityHitResult(Level pLevel) {
//        double d0 = Double.MAX_VALUE;
        Entity entity = null;
        if (this.entityPredicate == null) {
            return null;
        }
        List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, boundingBox, this.entityPredicate);
        if (!entities.isEmpty()) {
            entity = entities.get(0);
        }

        return entity == null ? null : new EntityHitResult(entity);
    }

    public boolean hasHitSomething() {
        return this.hitResult != null && this.hitResult.getType() != HitResult.Type.MISS;
    }

    public boolean hasHitEntity() {
        return this.hitResult != null && this.hitResult.getType() == HitResult.Type.ENTITY;
    }

    public boolean hasHitBlock() {
        return this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK;
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

    public double getProjectileSize() {
        return this.projectileSize;
    }

    public AABB getBoundingBox() {
        return boundingBox;
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


        Vec3 atob = b.subtract(a);
        Vec3 normal = new Vec3(0, 1, 0);
        Vec3 project = atob.subtract(normal.scale(atob.dot(normal)/normal.length()/normal.length()));

        Vec3 np = normal.scale(Math.tan(theta));
        Vec3 desire = project.normalize().add(np).normalize().scale(v);

        return desire;
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

    @Nullable
    public HitResult getHitResult() {
        return hitResult;
    }

    private void doHitTarget(ServerLevel serverLevel) {
        if (!hasHitEntity()) return;
        EntityHitResult entityHitResult = (EntityHitResult) hitResult;
        Entity entity = entityHitResult.getEntity();
        Entity gunnerEntity = serverLevel.getEntity(gunner);
        if (gunnerEntity == null) {
            entity.hurt(serverLevel.damageSources().mobAttack(null), damage);
        }
        else {
            entity.hurt(gunnerEntity.damageSources().mobAttack((LivingEntity) gunnerEntity), damage);
        }
    }
}
