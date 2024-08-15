package com.github.icecheesecat.shincolle.entity.util.attack;

import com.github.icecheesecat.shincolle.entity.util.ShipTickableAction;
import net.minecraft.world.entity.LivingEntity;

public class ShipMeleeAttack extends ShipTickableAction {

    private LivingEntity ship;
    private float AttackRange = 2.0f;

    public ShipMeleeAttack(LivingEntity ship) {
        super(1 * 20); // TODO configurable melee cooldown
        this.ship = ship;
    }

    public boolean inMeleeRange(LivingEntity entity) {
        if (entity == null) return false;
        return ship.distanceTo(entity) < AttackRange;
    }

    public void checkAndPerformMelee(LivingEntity entity) {
        if (this.inMeleeRange(entity) && this.checkCooldown()) {
            entity.hurt(ship.damageSources().mobAttack(ship), 5.0f);

            this.resetCooldown();
        }
    }

    public LivingEntity getShip() {
        return ship;
    }

    public float getAttackRange() {
        return AttackRange;
    }

    public void setAttackRange(float attackRange) {
        AttackRange = attackRange;
    }



    @Override
    public void tick() {
        this.tickCooldown();
    }
}
