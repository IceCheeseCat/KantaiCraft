package com.github.icecheesecat.kantaicraft.entityship.brain.sensor;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Set;
import java.util.function.Supplier;

public class ShipResourcesSensor extends Sensor<EntityShip> {

    public ShipResourcesSensor() {
    }

    @Override
    protected void doTick(ServerLevel pLevel, EntityShip pEntity) {
        shipResourceCheck(pEntity, ModMemoryModuleType.OUT_OF_FUEL.get(), pEntity::hasNoFuel);
        shipResourceCheck(pEntity, ModMemoryModuleType.OUT_OF_AMMO.get(), pEntity::notEnoughAmmo);
        shipResourceCheck(pEntity, ModMemoryModuleType.OUT_OF_AIRCRAFT.get(), pEntity::hasNoAirCraft);
    }

    private void shipResourceCheck(EntityShip playerEntityShip, MemoryModuleType<Unit> memory, Supplier<Boolean> condition) {
        if (condition.get()) {
            playerEntityShip.getBrain().setMemory(memory, Unit.INSTANCE);
        }
        else {
            playerEntityShip.getBrain().eraseMemory(memory);
        }
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(ModMemoryModuleType.OUT_OF_FUEL.get(),
                ModMemoryModuleType.OUT_OF_AMMO.get(),
                ModMemoryModuleType.OUT_OF_AIRCRAFT.get());
    }
}
