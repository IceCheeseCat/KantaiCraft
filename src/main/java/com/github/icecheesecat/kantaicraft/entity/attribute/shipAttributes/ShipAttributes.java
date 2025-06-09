package com.github.icecheesecat.kantaicraft.entity.attribute.shipAttributes;

import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ShipAttributes {

    public static final AttributeSupplier DESTROYER_CLASS = Mob.createMobAttributes()
            .add(ModAttribute.FIREPOWER.get(), 10.0d)
            .add(ModAttribute.TORPEDO.get(), 20.0d)
            .add(ModAttribute.ANTIAIR.get(), 40.0d)
            .add(ModAttribute.ASW.get(), 20.0d)
            .add(ModAttribute.LOS.get(), 45.0d)
            .add(ModAttribute.LUCK.get(), 10.0d)
            .add(Attributes.MAX_HEALTH, 100.0d)
            .add(ModAttribute.ARMOR.get(), 12.0d)
            .add(ModAttribute.EVASION.get(), 10.0d)
            .add(Attributes.MOVEMENT_SPEED, 0.25d)
            .add(ModAttribute.AIRCRAFT.get(), 0.0d)
            .add(ModAttribute.FUEL.get(), 10.0f)
            .add(ModAttribute.AMMO.get(), 12.0f)
            .add(Attributes.ATTACK_DAMAGE, 30.0d)
            .add(Attributes.ATTACK_SPEED)
            .add(ModAttribute.SLOT_SIZE.get(), 4.0d)
            .add(ModAttribute.SHIPSONAL_SPACE.get(), 3.0d)
            .build();

}
