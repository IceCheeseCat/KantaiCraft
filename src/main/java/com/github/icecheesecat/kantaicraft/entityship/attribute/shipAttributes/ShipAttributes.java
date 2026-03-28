package com.github.icecheesecat.kantaicraft.entityship.attribute.shipAttributes;

import com.github.icecheesecat.kantaicraft.entityship.entity.features.AttributeGrowth;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ShipAttributes {

    public static class Destroyer {
        public static final AttributeSupplier DEFAULT = Mob.createMobAttributes()
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
                .add(ModAttribute.MAX_AIRCRAFT.get(), 0.0d)
                .add(ModAttribute.MAX_FUEL.get(), 10000.0f)
                .add(ModAttribute.MAX_AMMO.get(), 200.0f)
                .add(Attributes.ATTACK_DAMAGE, 1.0d)
                .add(Attributes.ATTACK_SPEED)
                .build();
        public static final AttributeGrowth GROWTH = AttributeGrowth.builder()
                .with(ModAttribute.FIREPOWER.get(), 0.1d)
                .with(ModAttribute.TORPEDO.get(), 0.5d)
                .with(ModAttribute.ANTIAIR.get(), 0.3d)
                .with(Attributes.MAX_HEALTH, 0.1d)
                .with(Attributes.MOVEMENT_SPEED, 0.0001d)
                .with(ModAttribute.MAX_FUEL.get(), 50.0f)
                .with(ModAttribute.MAX_AMMO.get(), 1.0f)
                .with(Attributes.ATTACK_DAMAGE, 0.03d)
                .build();
    }


}
