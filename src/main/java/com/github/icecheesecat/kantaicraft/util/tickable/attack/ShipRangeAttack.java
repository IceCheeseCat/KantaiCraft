package com.github.icecheesecat.kantaicraft.util.tickable.attack;

import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import com.github.icecheesecat.kantaicraft.util.tickable.ShipTickableAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class ShipRangeAttack extends ShipTickableAction {

    private BasicEntityShip ship;
    private LivingEntity target;

    public ShipRangeAttack(BasicEntityShip ship) {
        super(4 * 20); // TODO configurable range cooldown
        this.ship = ship;
    }

    public void searchInRange() {
        if (this.ship.level().isClientSide) return;
        Level level = ship.level();
        // TODO search target
        Monster m = level.getNearestEntity(Monster.class, TargetingConditions.DEFAULT, ship, ship.getX(), ship.getY(), ship.getZ(), ship.getBoundingBox().inflate(this.ship.getAttributeValue(ModShipAttributes.LOS.get())));
        if (m != null) {
            target = m;
        }
    }

    public BasicEntityShip getShip() {
        return ship;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }
}
