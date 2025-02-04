package com.github.icecheesecat.kantaicraft.util;

import com.github.icecheesecat.kantaicraft.network.packet.TrajectoryPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Trajectory {

    final float dt = 0.01f;
    final int id;
    Vec3 pos;
    Vec3 vel;
    Vec3 acc;
    int alive = 4 * 20; // 10 second alive
    Vec3 prevPos;
    public static final int EULER_METHOD = 0;
    AABB boundingBox = null;
    public static final double DEFAULT_PROJECTILE_SIZE = 0.5d;
    double projSize = DEFAULT_PROJECTILE_SIZE;

    public Trajectory(Vec3 pos, Vec3 vel, Vec3 acc, int id, double projSize) {
        this.pos = this.prevPos = pos;
        this.vel = vel;
        this.acc = acc;
        this.id = id;
        this.projSize = projSize;
        boundingBox = AABB.ofSize(pos, 0.5d, 0.5d, 0.5d);
    }

    public Trajectory(Trajectory t) {
        this.pos = t.getPos();
        this.vel = t.getVel();
        this.acc = t.getAcc();
        this.id = t.getId();
    }

    public Trajectory(TrajectoryPacket packet) {
        this.pos = packet.pos;
        this.vel = packet.vel;
        this.acc = packet.acc;
        this.id = packet.id;
    }

    public Trajectory asCopy() {
        Trajectory n_t = new Trajectory(this.pos, this.vel, this.acc, this.id, this.projSize);
        n_t.alive = this.alive;

        return n_t;
    }

    public void tick(int method) {
        // euler method
        switch (method) {
            case 0:
                prevPos = pos;
                pos = pos.add(vel.scale(dt));
                vel = vel.add(acc.scale(dt));
                break;
            default:
                prevPos = pos;
                pos = pos.add(vel.scale(dt));
                vel = vel.add(acc.scale(dt));
                break;
        }

        // update bounding box position
        this.boundingBox = AABB.ofSize(pos, projSize, projSize, projSize);

        alive--;
//        this.print();
    }

    public boolean checkTimeout() {
        return alive <= 0;
    }

    public HitResult getHitResult(Predicate<Entity> pFilter, Level pLevel) {
        HitResult hitresult = pLevel.clip(new ClipContext(prevPos, pos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        if (hitresult.getType() != HitResult.Type.MISS) {
            pos = hitresult.getLocation();
        }

        HitResult hitresult1 = getEntityHitResult(pLevel, pFilter);
        if (hitresult1 != null) {
            hitresult = hitresult1;
        }

        return hitresult;
    }

    @Nullable
    public EntityHitResult getEntityHitResult(Level pLevel, Predicate<Entity> pFilter) {
//        double d0 = Double.MAX_VALUE;
        Entity entity = null;
        List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, boundingBox, pFilter);
        if (!entities.isEmpty()) {
            entity = entities.get(0);
        }

        return entity == null ? null : new EntityHitResult(entity);
    }


    public int getId() {
        return id;
    }

    public Vec3 getPos() {
        return pos;
    }

    public Vec3 getVel() {
        return vel;
    }

    public Vec3 getAcc() {
        return acc;
    }

    public void setPos(Vec3 pos) {
        this.pos = pos;
    }

    public void setVel(Vec3 vel) {
        this.vel = vel;
    }

    public void setAcc(Vec3 acc) {
        this.acc = acc;
    }

    public void print() {
        System.out.println(this.id + ": ");
        System.out.print(this.pos + ", ");
        System.out.print(this.vel + ", ");
        System.out.println(this.acc);
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

}
