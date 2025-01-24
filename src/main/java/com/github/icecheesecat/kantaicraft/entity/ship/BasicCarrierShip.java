package com.github.icecheesecat.kantaicraft.entity.ship;

import com.github.icecheesecat.kantaicraft.brain.ship.CarrierBrain;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

public abstract class BasicCarrierShip extends BasicEntityShip {

    protected BasicCarrierShip(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Brain<BasicEntityShip> getBrain() {
        return (Brain<BasicEntityShip>) this.brain;
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dyn) {
        return CarrierBrain.makeBrain(this, dyn);
    }
}
