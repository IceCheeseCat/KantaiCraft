package com.github.icecheesecat.kantaicraft.tickable.attack;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.tickable.ShipTickableAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public abstract class ShipRangeAttack extends ShipTickableAction {

    EntityShip entityShip;
    LivingEntity target;

    protected static final Vec3 GRAVITY = new Vec3(0, -9.8f, 0);

    public ShipRangeAttack(EntityShip entityShip, int cooldown) {
        super(cooldown);
        this.entityShip = entityShip;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }
}
