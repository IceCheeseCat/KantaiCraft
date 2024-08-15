package com.github.icecheesecat.shincolle.entity.util.attack;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class Trajectory {

    final float dt = 0.1f;
    Vec3 pos;
    Vec3 vel;
    Vec3 acc;
    int alive = 20 * 20; // 10 second alive
    public static final int TICK_METHOD = 0;
    boolean hit = false;

    public void init(Vec3 i, Vec3 v, Vec3 a) {
        pos = new Vec3(i.x, i.y, i.z);
        vel = new Vec3(v.x, v.y, v.z);
        acc = new Vec3(a.x, a.y, a.z);
    }

    public void tick(int method) {
        // euler method
        switch (method) {
            case 0:
                pos.add(vel.multiply(dt,dt,dt));
                vel.add(acc.multiply(dt,dt,dt));
                break;
            default:
                pos.add(vel.multiply(dt,dt,dt));
                vel.add(acc.multiply(dt,dt,dt));
                break;
        }

        alive--;
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

}
