package com.github.icecheesecat.kantaicraft.stats;

import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ShipClassAttributes {

    public static final AttributeSupplier DESTROYER_CLASS = Mob.createMobAttributes()
            .add(ModShipAttributes.FIREPOWER.get(), 10.0d)
            .add(ModShipAttributes.TORPEDO.get(), 20.0d)
            .add(ModShipAttributes.ANTIAIR.get(), 40.0d)
            .add(ModShipAttributes.ASW.get(), 20.0d)
            .add(ModShipAttributes.LOS.get(), 45.0d)
            .add(ModShipAttributes.LUCK.get(), 10.0d)
            .add(Attributes.MAX_HEALTH, 24.0d)
            .add(ModShipAttributes.ARMOR.get(), 12.0d)
            .add(ModShipAttributes.EVASION.get(), 10.0d)
            .add(Attributes.MOVEMENT_SPEED, 0.25d)
            .add(ModShipAttributes.AIRCRAFT.get(), 0.0d)
            .add(ModShipAttributes.FUEL.get(), 10.0f)
            .add(ModShipAttributes.AMMO.get(), 12.0f)
            .add(Attributes.ATTACK_DAMAGE, 1.0d)
            .add(Attributes.ATTACK_SPEED)
            .add(ModShipAttributes.SLOT_SIZE.get(), 2.0d)
            .build();

}
