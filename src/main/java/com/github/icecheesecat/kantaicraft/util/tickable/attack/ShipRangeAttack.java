package com.github.icecheesecat.kantaicraft.util.tickable.attack;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.util.tickable.ShipTickableAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class ShipRangeAttack extends ShipTickableAction {

    private BasicEntityShip ship;
    private LivingEntity target;

    public ShipRangeAttack(BasicEntityShip ship, int cooldown) {
        super(cooldown);
        this.ship = ship;
    }

    public void searchInRange() {
        if (this.ship.level().isClientSide) return;
        Level level = ship.level();
        // TODO search target
        Monster m = level.getNearestEntity(Monster.class, TargetingConditions.DEFAULT, ship, ship.getX(), ship.getY(), ship.getZ(), ship.getBoundingBox().inflate(this.ship.getAttributeValue(ModAttribute.LOS.get())));
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
