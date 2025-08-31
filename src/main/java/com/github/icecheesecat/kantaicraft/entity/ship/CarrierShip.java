package com.github.icecheesecat.kantaicraft.entity.ship;

import com.github.icecheesecat.kantaicraft.entity.brain.ship.CarrierBrain;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class CarrierShip extends EntityShip {

    public static final List<EquipmentType> ATTACKABLE_TYPES = ImmutableList.of(EquipmentType.AIRCRAFT_FIGHTER);

    protected CarrierShip(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, ShipClass.AIRCRAFT_CARRIER, level, ATTACKABLE_TYPES);
    }

    @Override
    public Brain<EntityShip> getBrain() {
        return (Brain<EntityShip>) this.brain;
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dyn) {
        return CarrierBrain.makeBrain(this, dyn);
    }
}
