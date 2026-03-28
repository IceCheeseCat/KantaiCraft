package com.github.icecheesecat.kantaicraft.entityship.entity.cannoship;

import com.github.icecheesecat.kantaicraft.entityship.attribute.shipAttributes.ShipAttributes;
import com.github.icecheesecat.kantaicraft.entityship.entity.ShipClass;
import com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.destroyer.EntityShipDefaultEquipments;
import com.github.icecheesecat.kantaicraft.entityship.entity.features.AttributeGrowth;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentClass;
import com.github.icecheesecat.kantaicraft.equipment.SlotChecker;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public abstract class DestroyerEntityShip extends CannonEntityShip {

    private static final SlotChecker GENERAL_DESTROYER_SLOT_CHECKER = SlotChecker.create(ImmutableSet.of(EquipmentClass.SMALL_CANNON, EquipmentClass.RADAR));

    public DestroyerEntityShip(EntityType<? extends PathfinderMob> entityType, Level level, String name) {
        super(entityType, ShipClass.DESTROYER, level, name);
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
        EntityShipDefaultEquipments.DESTROYER.forEach((id, factory) -> {
            equipmentHandler.setEquipment(id, factory.get(), this);
        });
    }

    @Override
    protected int defineFuelTankSize() {
        return 64000;
    }

    @Override
    protected int defineFuelUsage() {
        return 10;
    }

    @Override
    protected AttributeGrowth defineGrowth() {
        return ShipAttributes.Destroyer.GROWTH;
    }
}
