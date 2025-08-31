package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.CannonShip;
import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class SoutBurnOut extends Behavior<EntityShip> {
    public SoutBurnOut() {
        super(ImmutableMap.of(ModMemoryModuleType.OUT_OF_FUEL.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected void tick(ServerLevel pLevel, EntityShip pOwner, long pGameTime) {
        if (pGameTime % 60 == 0) {
            System.out.println(pOwner.toString() + ", burn out of fuel");
        }
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        return true;
    }
}
