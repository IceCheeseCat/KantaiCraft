package com.github.icecheesecat.kantaicraft.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.customObjects.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicCannonShip;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class SoutBurnOut extends Behavior<BasicCannonShip> {
    public SoutBurnOut() {
        super(ImmutableMap.of(ModMemoryModuleType.OUT_OF_FUEL.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected void tick(ServerLevel pLevel, BasicCannonShip pOwner, long pGameTime) {
        if (pGameTime % 60 == 0) {
            System.out.println(pOwner.toString() + ", burn out of fuel");
        }
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, BasicCannonShip pOwner) {
        return true;
    }
}
