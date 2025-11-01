package com.github.icecheesecat.kantaicraft.entityship.entity;

import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.DestroyerDefaultEquipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.equipment.SlotChecker;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipResult;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public abstract class DestroyerEntityShip extends CannonEntityShip {

    private static final SlotChecker GENERAL_DESTROYER_SLOT_CHECKER = SlotChecker.create(ImmutableSet.of(EquipmentType.SMALL_CANNON, EquipmentType.RADAR));

    public DestroyerEntityShip(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, ShipClass.DESTROYER, level);
    }

    @Override
    public float getAmmoCost() {
        return 1.0f;
    }

    @Override
    public SlotChecker getSlotChecker(int index) {
        return GENERAL_DESTROYER_SLOT_CHECKER;
    }

    @Override
    protected void defaultEquipments(EquipmentHandler equipmentHandler) {
        DestroyerDefaultEquipment.DESTROYER.forEach((id, factory) -> {
            equipmentHandler.setEquipment(id, factory.get(), this);
        });
    }
}
