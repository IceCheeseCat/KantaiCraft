package com.github.icecheesecat.kantaicraft.util.tickable.attack;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.util.tickable.ShipTickableAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class ShipRangeAttack extends ShipTickableAction {

    EntityShip ship;
    LivingEntity target;

    protected static final Vec3 GRAVITY = new Vec3(0, -9.8f, 0);

    public ShipRangeAttack(EntityShip ship, int cooldown) {
        super(cooldown);
        this.ship = ship;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }
}
