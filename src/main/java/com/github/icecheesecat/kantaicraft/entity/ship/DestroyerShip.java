package com.github.icecheesecat.kantaicraft.entity.ship;

import com.github.icecheesecat.kantaicraft.entity.stance.EntityStance;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.equipment.SlotChecker;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.google.common.collect.ImmutableSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class DestroyerShip extends CannonShip {

    private static final SlotChecker GENERAL_DESTROYER_SLOT_CHECKER = SlotChecker.create(ImmutableSet.of(EquipmentType.SMALL_CANNON, EquipmentType.RADAR));

    public DestroyerShip(EntityType<? extends PathfinderMob> entityType, Level level, EntityStance entityStance) {
        super(entityType, ShipClass.DESTROYER, level, entityStance);
    }

    @Override
    public float getAmmoCost() {
        return 1.0f;
    }

    @Override
    public SlotChecker getSlotChecker(int index) {
        return GENERAL_DESTROYER_SLOT_CHECKER;
    }

}
