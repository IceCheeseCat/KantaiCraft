package com.github.icecheesecat.kantaicraft.entity.ship;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.util.ShipFields;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class BasicDestroyerShip extends BasicCannonShip {

    private static final AttributeSupplier DESTROYER_GROWTH = new AttributeSupplier.Builder()
            .add(ModAttribute.FIREPOWER.get(), 0.5d)
            .add(ModAttribute.TORPEDO.get(), 1.0d)
            .add(ModAttribute.ANTIAIR.get(), 1.0f)
            .add(ModAttribute.ASW.get(), 1.0d)
            .add(ModAttribute.LOS.get(), 0.25d)
            .add(Attributes.MAX_HEALTH, 0.2d)
            .add(ModAttribute.ARMOR.get(), 0.1d).build();

    protected BasicDestroyerShip(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public AttributeSupplier getGrowth() {
        return DESTROYER_GROWTH;
    }

    @Override
    public float getAmmoCost() {
        return 1.0f;
    }

    @Override
    public ShipFields.ShipClass getShipClass() {
        return ShipFields.ShipClass.DESTROYER;
    }

    @Override
    public List<Equipment> evaluateEquipments(List<Equipment> equipments) {
        return equipments.stream().filter(equipment ->
            equipment.getType() == EquipmentType.CANNON && equipment.getStat(EquipmentStatType.CANNON_SIZE) == 0
        ).toList();
    }

}
