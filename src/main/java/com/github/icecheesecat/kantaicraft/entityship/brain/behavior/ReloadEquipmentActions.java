package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.tickable.EquipmentActionHandler;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class ReloadEquipmentActions extends Behavior<EntityShip> {

    boolean reloadWhenOutOfFuel;
    public ReloadEquipmentActions(boolean reloadWhenOutOfFuel) {
        super(ImmutableMap.of(ModMemoryModuleType.ACTION_HANDLER.get(), MemoryStatus.VALUE_PRESENT));
        this.reloadWhenOutOfFuel = reloadWhenOutOfFuel;
    }

    @Override
    protected void tick(ServerLevel pLevel, EntityShip pOwner, long pGameTime) {
        pOwner.getBrain().getMemory(ModMemoryModuleType.ACTION_HANDLER.get()).ifPresent(EquipmentActionHandler::tick);
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        return reloadWhenOutOfFuel || pEntity.getBrain().getMemory(ModMemoryModuleType.OUT_OF_FUEL.get()).isEmpty();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        return reloadWhenOutOfFuel || pOwner.getBrain().getMemory(ModMemoryModuleType.OUT_OF_FUEL.get()).isEmpty();
    }
}
