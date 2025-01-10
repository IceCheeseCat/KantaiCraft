package com.github.icecheesecat.kantaicraft.entity.ship;

import com.github.icecheesecat.kantaicraft.brain.ship.CannonShipBrain;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.github.icecheesecat.kantaicraft.customObjects.ModShipAttributes;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public abstract class BasicDestroyerShip extends BasicCannonShip {

    private static final AttributeSupplier DESTROYER_GROWTH = new AttributeSupplier.Builder()
            .add(ModShipAttributes.FIREPOWER.get(), 0.5d)
            .add(ModShipAttributes.TORPEDO.get(), 1.0d)
            .add(ModShipAttributes.ANTIAIR.get(), 1.0f)
            .add(ModShipAttributes.ASW.get(), 1.0d)
            .add(ModShipAttributes.LOS.get(), 0.25d)
            .add(Attributes.MAX_HEALTH, 0.2d)
            .add(ModShipAttributes.ARMOR.get(), 0.1d).build();

    protected BasicDestroyerShip(EntityType<? extends PathfinderMob> entityType, Level level, EquipmentSlots equipmentSlot) {
        super(entityType, level, equipmentSlot);
    }

    @Override
    public AttributeSupplier getGrowth() {
        return DESTROYER_GROWTH;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public float getAmmoCost() {
        return 1.0f;
    }

}
