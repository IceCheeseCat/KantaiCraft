package com.github.icecheesecat.shincolle.entity.util.attack;

import com.github.icecheesecat.shincolle.entity.util.BasicCanonShip;
import com.github.icecheesecat.shincolle.entity.util.BasicEntityShip;
import com.github.icecheesecat.shincolle.weaponary.canon.Canon;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ShipCanonAttack extends ShipRangeAttack {

    Canon canon;
    List<Trajectory> trajectories = new ArrayList<>();

    public ShipCanonAttack(BasicCanonShip ship, Canon canon) {
        super(ship);
        this.canon = canon;
    }

    public void checkAndPerformCanon(LivingEntity target) {
        if (this.checkCooldown()) {
            if (target != null && target.isAlive()) {
                Trajectory t = new Trajectory();
                LivingEntity ship = getShip();
                Vec3 initPos = new Vec3(ship.getX(), ship.getEyeY(), ship.getZ());

                float canon_vel = canon.getCanon_vel();
                Vec3 fire_dir = target.position().subtract(ship.position()).normalize().add(0,1,0).normalize().multiply(canon_vel, canon_vel, canon_vel);

                Vec3 gravity = new Vec3(0, 1, 0);
                t.init(initPos, fire_dir, gravity);
                trajectories.add(t);

                this.resetCooldown();
            }
        }
    }

    public void doHitTarget(Entity entity) {
        BasicEntityShip ship = getShip();
        entity.hurt(ship.damageSources().mobProjectile(entity, ship), ship.getShipAttrs().getFirePower());
    }


    protected void tickTrajectories() {
        List<Trajectory> invalids = new ArrayList<>();
        for (var t: trajectories) {
            t.tick(0);

            HitResult hitResult = t.findHitResult(getShip().level());
            if (hitResult.getType() != HitResult.Type.MISS) {
                if (hitResult instanceof EntityHitResult ehr) {
                    Entity entity = ehr.getEntity();
                    doHitTarget(entity);
                }
                invalids.add(t);
            }

            if (t.checkTimeout()) {
                invalids.add(t);
            }
        }

        trajectories.removeAll(invalids);
    }


    @Override
    public void tick() {
        this.tickCooldown();
        this.tickTrajectories();
    }
}

