package com.github.icecheesecat.kantaicraft.entityship.brain.sensor;

import com.github.icecheesecat.kantaicraft.capability.equipment.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.tickable.EquipmentActionHandler;
import com.github.icecheesecat.kantaicraft.tickable.ShipTickableAction;
import com.github.icecheesecat.kantaicraft.tickable.attack.CannonAttack;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Set;

public class ShipEquipmentSensor extends Sensor<EntityShip> {

    public ShipEquipmentSensor() {
        super();
    }

    @Override
    protected void doTick(ServerLevel pLevel, EntityShip pEntity) {
        pEntity.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
            equipmentHandler -> {
//                System.out.println("has equipment");
                var optional = pEntity.getBrain().getMemory(ModMemoryModuleType.ACTION_HANDLER.get());
                if (!optional.isPresent()) {
                    pEntity.getBrain().setMemory(ModMemoryModuleType.ACTION_HANDLER.get(), new EquipmentActionHandler(pEntity, equipmentHandler));
                }

                pEntity.getBrain().getMemory(ModMemoryModuleType.ACTION_HANDLER.get()).ifPresent(
                    shipTickableActions -> {
                        for (int i = 0; i < shipTickableActions.size(); i++) {
                           updateAction(pEntity, i, equipmentHandler, shipTickableActions);
                        }

                    }
                );
            }
        );
    }

    /**
     * Update {@link EquipmentActionHandler} if {@link EquipmentHandler} has new equipment installed
     */
    public void updateAction(EntityShip entityShip, int index, EquipmentHandler equipmentHandler, EquipmentActionHandler equipmentActionHandler) {
        if (!equipmentHandler.isDirty(index)) {
            return;
        }

        Equipment equipment = equipmentHandler.getEquipments().get(index);
        switch (equipment.getEquipmentClass()) {
            case SMALL_CANNON -> equipmentActionHandler.set(index, new CannonAttack(entityShip, equipment));
            case NONE -> equipmentActionHandler.set(index, ShipTickableAction.NULL);
            default -> throw new RuntimeException("Unknown equipment type at " + entityShip);
        }
        equipmentHandler.setNotDirty(index);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(ModMemoryModuleType.ACTION_HANDLER.get());
    }


}
