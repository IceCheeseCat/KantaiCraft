package com.github.icecheesecat.kantaicraft.entityship.equipment;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.equipment.entity.EquipmentEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class EquipmentEntitySystem {

    EntityShip entityShip;
    Level level;
    Map<Integer, EquipmentEntity> equipmentEntities = new HashMap<>();

    public EquipmentEntitySystem(EntityShip entityShip, Level level) {
        this.entityShip = entityShip;
        this.level = level;
    }

    public void spawnAllEntities() {
        this.removeAllEntities();
        this.entityShip.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
                equipmentHandler -> {
                    for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
                        Equipment equipment = equipmentHandler.getEquipment(i);
                        if (equipment == null || equipment.getId() == -1) {
                            continue;
                        }
                        EquipmentEntity equipmentEntity = equipmentToEntity(i, equipmentHandler.getEquipment(i));
                        this.addEntity(i, equipmentEntity);
                    }
                }
        );
        this.spawnEntitiesToLevel();
    }

    private void addEntity(int i, EquipmentEntity equipmentEntity) {
        if (this.equipmentEntities.containsKey(i)) {
            this.removeEntity(i);
        }
        this.equipmentEntities.put(i, equipmentEntity);
    }

    private void spawnEntitiesToLevel() {
        this.equipmentEntities.forEach((i, equipmentEntity) -> {
            this.level.addFreshEntity(equipmentEntity);
        });
    }

    public void removeEntity(int i) {
        if (this.equipmentEntities.containsKey(i)) {
            this.equipmentEntities.get(i).discard();
        }
    }

    private void removeAllEntities() {
        this.equipmentEntities.forEach(
            (i, equipmentEntity) -> {
                equipmentEntity.discard();
            }
        );
    }

    private EquipmentEntity equipmentToEntity(int i, Equipment equipment) {

        var entityType = EquipmentManager.getEquipmentEntityType(equipment.getId());
        assert entityType != null;
        var equipmentEntity = entityType.get().create(this.level);
        assert equipmentEntity != null;
        equipmentEntity.setRelativeToEntity(this.entityShip, this.entityShip.getPhysicalEquipmentSlot().getSlotPosition(i));
        return equipmentEntity;

    }

}
