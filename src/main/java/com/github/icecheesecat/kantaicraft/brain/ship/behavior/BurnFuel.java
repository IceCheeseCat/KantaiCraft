package com.github.icecheesecat.kantaicraft.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.customObjects.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class BurnFuel extends Behavior<BasicEntityShip> {

    private static final long TIME_PERIOD = 200L;

    public BurnFuel() {
        super(ImmutableMap.of(ModMemoryModuleType.OUT_OF_FUEL.get(), MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected void start(ServerLevel serverLevel, BasicEntityShip basicEntityShip, long gametime) {
        basicEntityShip.getBrain().eraseMemory(ModMemoryModuleType.OUT_OF_FUEL.get());
    }

    @Override
    protected void tick(ServerLevel serverLevel, BasicEntityShip basicEntityShip, long gametime) {
        if (gametime % TIME_PERIOD == 0) {
            basicEntityShip.setFuel(basicEntityShip.getFuel() - 0.1f); // 1 second minus 0.1 bucket
            System.out.println(basicEntityShip.getShipName() + ", " + basicEntityShip.getFuel());
        }
    }

    @Override
    protected void stop(ServerLevel serverLevel, BasicEntityShip basicEntityShip, long gametime) {
        basicEntityShip.getBrain().setMemory(ModMemoryModuleType.OUT_OF_FUEL.get(), Unit.INSTANCE);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, BasicEntityShip basicEntityShip) {
        return !basicEntityShip.hasFuel();
    }
}
