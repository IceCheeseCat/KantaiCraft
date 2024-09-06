package com.github.icecheesecat.kantaicraft.util;

import com.github.icecheesecat.kantaicraft.network.packet.TrajectoryPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class Trajectory {

    final float dt = 0.01f;
    final int id;
    Vec3 pos;
    Vec3 vel;
    Vec3 acc;
    int alive = 4 * 20; // 10 second alive
    public static final int EULER_METHOD = 0;

    public Trajectory(Vec3 pos, Vec3 vel, Vec3 acc, int id) {
        this.pos = pos;
        this.vel = vel;
        this.acc = acc;
        this.id = id;
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
        Trajectory n_t = new Trajectory(this.pos, this.vel, this.acc, this.id);
        n_t.alive = this.alive;

        return n_t;
    }

    public void tick(int method) {
        // euler method
        switch (method) {
            case 0:
                pos = pos.add(vel.scale(dt));
                vel = vel.add(acc.scale(dt));
                break;
            default:
                pos = pos.add(vel.scale(dt));
                vel = vel.add(acc.scale(dt));
                break;
        }

        alive--;
//        this.print();
    }

    public boolean checkTimeout() {
        return alive <= 0;
    }

    public HitResult findHitResult(Level level) {
        return getHitResult(pos, this::canHitEntity, vel, level);
    }

    private boolean canHitEntity(Entity entity) {
        return entity.canBeHitByProjectile();
    }

    public static HitResult getHitResult(Vec3 pos, Predicate<Entity> p, Vec3 delta, Level level) {
        Vec3 vec3 = pos.add(delta);
        HitResult hitresult = level.clip(new ClipContext(pos, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        if (hitresult.getType() != HitResult.Type.MISS) {
            vec3 = hitresult.getLocation();
        }

        HitResult hitresult1 = getEntityHitResult(level, pos, vec3, AABB.ofSize(pos, 2.0d, 2.0d, 2.0d), p);
        if (hitresult1 != null) {
            hitresult = hitresult1;
        }

        return hitresult;
    }

    @Nullable
    private static EntityHitResult getEntityHitResult(Level level, Vec3 pos, Vec3 vec3, AABB paabb, Predicate<Entity> predicate) {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;

        for(Entity entity1 : level.getEntities((Entity) null, paabb, predicate)) {
            AABB aabb = entity1.getBoundingBox().inflate((double)0.3f);
            Optional<Vec3> optional = aabb.clip(pos, vec3);
            if (optional.isPresent()) {
                double d1 = pos.distanceToSqr(optional.get());
                if (d1 < d0) {
                    entity = entity1;
                    d0 = d1;
                }
            }
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

    public static double calculateAngle(Vec3 start, Vec3 end, Vec3 gravity, float canon_vel) {

        double d, h, g, v;
        d = Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.z - end.z, 2));
        h = end.y - start.y;
        g = gravity.y;
        v = canon_vel;

        double A, B, C;
        A = d*d*d*d*g*g/v/v/v/v;
        B = - (4*d*d + 4*g*h*d*d/v/v);
        C = 4*h*h + 4*d*d;

//        System.out.println(A);
//        System.out.println(B);
//        System.out.println(C);

        double x0 = (-B + Math.sqrt(B * B - 4 * A * C)) / 2 / A;
        double x1 = (-B - Math.sqrt(B * B - 4 * A * C)) / 2 / A;

//        System.out.println(x0);
//        System.out.println(x1);

        double theta0 = Math.acos(1/Math.sqrt(x0));
        double theta1 = Math.acos(1/Math.sqrt(x1));

//        System.out.println();
//        System.out.println(theta0);
//        System.out.println(theta1);

        if (Double.isNaN(theta0) && Double.isNaN(theta1)) {
            return Double.NaN;
        }
        else {
            if (Double.isNaN(theta0)) {
                return theta1;
            }
            else if (Double.isNaN(theta1)) {
                return theta0;
            }
            else {
                if (theta0 < theta1) {
                    return theta0;
                }
                else {
                    return theta1;
                }
            }
        }
    }

    public static Vec3 calculateFireDir(Vec3 start, Vec3 end, double angle, float canon_vel) {
        Vec3 r = Vec3.ZERO.add(end.x, 0, end.z).subtract(start.x, 0, start.z);
        Vec3 r1 = r.add(0, r.length() * Math.sin(angle), 0);
        return r1.normalize().scale(canon_vel);
    }
}
