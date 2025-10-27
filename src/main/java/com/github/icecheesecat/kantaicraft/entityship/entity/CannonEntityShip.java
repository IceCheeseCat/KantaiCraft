package com.github.icecheesecat.kantaicraft.entityship.entity;

import com.github.icecheesecat.kantaicraft.entityship.brain.CannonShipBrain;
import com.github.icecheesecat.kantaicraft.entityship.brain.HostileCannonShipBrain;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class CannonEntityShip extends EntityShip {

    public static final List<EquipmentType> EQUITABLE_TYPES = ImmutableList.of(EquipmentType.SMALL_CANNON);

    public CannonEntityShip(EntityType<? extends PathfinderMob> entityType, ShipClass shipClass, Level level) {
        super(entityType, shipClass, level, EQUITABLE_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> pDynamic) {
        return !this.isHostileSide() ? CannonShipBrain.makeBrain(this, pDynamic) :
            HostileCannonShipBrain.makeBrain(this, pDynamic);
    }
}
