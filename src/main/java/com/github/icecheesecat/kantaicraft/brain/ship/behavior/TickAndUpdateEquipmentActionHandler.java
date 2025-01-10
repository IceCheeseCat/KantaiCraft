package com.github.icecheesecat.kantaicraft.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.customObjects.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.util.tickable.EquipmentActionHandler;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class TickAndUpdateEquipmentActionHandler extends Behavior<BasicEntityShip> {

    public TickAndUpdateEquipmentActionHandler() {
        super(ImmutableMap.of(ModMemoryModuleType.ACTION_HANDLER.get(), MemoryStatus.REGISTERED));
    }

    @Override
    protected void start(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        pEntity.getBrain().setMemory(ModMemoryModuleType.ACTION_HANDLER.get(), new EquipmentActionHandler(pEntity, pEntity.getEquipmentSlots()));
    }

    @Override
    protected void tick(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        pEntity.getBrain().getMemory(ModMemoryModuleType.ACTION_HANDLER.get()).ifPresent(EquipmentActionHandler::tick);
        this.updateAttackAction(pEntity);
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        return true;
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }

    private void updateAttackAction(BasicEntityShip ship) {
        var equipmentSlots = ship.getEquipmentSlots();
        for (int i = 0; i < equipmentSlots.getSlotSize(); i++) {
            if (equipmentSlots.isDirty(i)) {
                Equipment equipment = equipmentSlots.getEquipment(i);
                var opHandler = ship.getBrain().getMemory(ModMemoryModuleType.ACTION_HANDLER.get());
                if (opHandler.isPresent()) {
                    opHandler.get().resetAction(i);
                }

                equipmentSlots.setNotDirty(i);
            }
        }
    }
}
