package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class BurnFuel extends Behavior<EntityShip> {

    private static final long TIME_PERIOD = 200L;

    public BurnFuel() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel serverLevel, EntityShip entityShip, long gametime) {
        entityShip.getBrain().eraseMemory(ModMemoryModuleType.OUT_OF_FUEL.get());
    }

    @Override
    protected void tick(ServerLevel serverLevel, EntityShip entityShip, long gametime) {
        if (gametime % TIME_PERIOD == 0) {
            entityShip.setFuel(entityShip.getFuel() - 0.1f); // 1 second minus 0.1 bucket
            System.out.println(entityShip + ", " + entityShip.getFuel());
        }
    }

    @Override
    protected void stop(ServerLevel serverLevel, EntityShip entityShip, long gametime) {
        entityShip.getBrain().setMemory(ModMemoryModuleType.OUT_OF_FUEL.get(), Unit.INSTANCE);
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        return true;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EntityShip entityShip) {
        return entityShip.hasFuel();
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }
}
