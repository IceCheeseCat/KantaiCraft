package com.github.icecheesecat.kantaicraft.entity.ship.destroyer;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicCannonShip;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicDestroyerShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.SlotChecker;
import com.github.icecheesecat.kantaicraft.util.ShipFields;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

import java.util.List;

public class EntityDestroyerIClass extends BasicDestroyerShip {

    public EntityDestroyerIClass(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public double getPhysicalTurnRate() {
        return 0;
    }

    @Override
    public double getPhysicalSpeed() {
        return 0;
    }

    @Override
    public ShipFields.ShipName getShipName() {
        return ShipFields.ShipName.DestroyerIClass;
    }

    @Override
    protected void initEquipments() {

    }
}
