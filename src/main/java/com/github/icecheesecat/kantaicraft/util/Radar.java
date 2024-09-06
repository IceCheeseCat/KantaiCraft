package com.github.icecheesecat.kantaicraft.util;

import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import com.github.icecheesecat.kantaicraft.util.tickable.ShipTickableAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Radar extends ShipTickableAction {

    private final List<LivingEntity> targets;
    private BasicEntityShip ship;

    public Radar(BasicEntityShip ship, int cooldown) {
        super(cooldown);
        this.ship = ship;
        this.targets = new ArrayList<>();
    }

    // scan with cooldown
    private void normalScan(Level level) {
        if (!level.isClientSide) {
            List<Entity> list = level.getEntities(this.ship, this.ship.getBoundingBox().inflate(this.ship.getAttributeValue(ModShipAttributes.LOS.get())), isEnemy());

            this.targets.clear();
            double r = Math.pow(this.ship.getAttributeValue(ModShipAttributes.LOS.get()), 2);
            for (var e: list) {
                if (this.ship.distanceToSqr(e) < r) {
                    targets.add((LivingEntity) e);
                }
            }
        }
    }

    // TODO enemy judge
    // has to be living entities
    private static Predicate<Entity> isEnemy() {
        return (e) -> {
            if (e instanceof Monster) {
                return true;
            }
            else {
                return false;
            }
        };
    }

    @Override
    public void tick() {
        this.tickCooldown();
        if (checkCooldown()) {
            normalScan(this.ship.level());

            resetCooldown();
        }
    }

    public List<LivingEntity> getTargets() {
        return targets;
    }

    @Nullable
    public LivingEntity getClosestTarget() {
        LivingEntity entity = null;
        float dis = Float.POSITIVE_INFINITY;
        for (var t : targets) {
            if (t.distanceTo(this.ship) < dis) {
                entity = t;
            }
        }

        return entity;
    }

    @Nullable
    public LivingEntity getRandomTarget() {
        int size = targets.size();
        int rand_i = this.ship.getRandom().nextInt(size);
        if (targets.get(rand_i).isAlive()) {
            return targets.get(rand_i);
        }
        else {
            return null;
        }
    }

    @Nullable
    public LivingEntity getIndexedTarget(int index) {
        if (index < 0 || index >= targets.size()) {
            return null;
        }
        else {
            return targets.get(index);
        }
    }
}
