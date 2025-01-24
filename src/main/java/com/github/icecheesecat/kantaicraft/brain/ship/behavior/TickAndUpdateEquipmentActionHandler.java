package com.github.icecheesecat.kantaicraft.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.capability.EquipmentProvider;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.util.tickable.EquipmentActionHandler;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;

public class TickAndUpdateEquipmentActionHandler extends Behavior<BasicEntityShip> {

    public TickAndUpdateEquipmentActionHandler() {
        super(ImmutableMap.of(ModMemoryModuleType.ACTION_HANDLER.get(), MemoryStatus.REGISTERED));
    }

    @Override
    protected void start(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        pEntity.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).ifPresent(equipmentHandler ->
            pEntity.getBrain().setMemory(ModMemoryModuleType.ACTION_HANDLER.get(), new EquipmentActionHandler(pEntity, equipmentHandler))
        );
    }

    @Override
    protected void tick(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        // tick actions
        Optional<EquipmentActionHandler> opHandler = pEntity.getBrain().getMemory(ModMemoryModuleType.ACTION_HANDLER.get());
        opHandler.ifPresent(EquipmentActionHandler::tick);
        // update action if equipment is changed
        opHandler.ifPresent(shipTickableActions -> this.updateAttackAction(pEntity, shipTickableActions));
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        return true;
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }

    private void updateAttackAction(BasicEntityShip ship, EquipmentActionHandler actionHandler) {
        ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).ifPresent(equipmentHandler -> {
            for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
                if (equipmentHandler.isDirty(i)) {
                    actionHandler.updateAction(i);
                    equipmentHandler.setNotDirty(i);
                }
            }
        });

    }
}
