package com.github.icecheesecat.kantaicraft.entityship.brain.sensor;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.tickable.EquipmentActionHandler;
import com.github.icecheesecat.kantaicraft.tickable.ShipTickableAction;
import com.github.icecheesecat.kantaicraft.tickable.attack.CannonShipAttack;
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
                           updateAction(pEntity, equipmentHandler, shipTickableActions, i);
                        }

                    }
                );
            }
        );
    }

    /**
     * Update {@link EquipmentActionHandler} if {@link EquipmentHandler} has new equipment installed
     */
    public void updateAction(EntityShip entityShip, EquipmentHandler equipmentHandler, EquipmentActionHandler equipmentActionHandler, int i) {
        if (!equipmentHandler.isDirty(i)) {
            return;
        }

        Equipment equipment = equipmentHandler.getEquipments().get(i);
        switch (equipment.getType()) {
            case SMALL_CANNON -> equipmentActionHandler.set(i, new CannonShipAttack(entityShip, equipment, (int) equipment.getStat(EquipmentStatType.CANNON_COOLDOWN)));
            case NONE -> equipmentActionHandler.set(i, ShipTickableAction.NULL);
            default -> throw new RuntimeException("Unknown equipment type at " + entityShip);
        }
        equipmentHandler.setNotDirty(i);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(ModMemoryModuleType.ACTION_HANDLER.get());
    }


}
