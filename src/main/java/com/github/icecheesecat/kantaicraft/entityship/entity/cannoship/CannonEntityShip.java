package com.github.icecheesecat.kantaicraft.entityship.entity.cannoship;

import com.github.icecheesecat.kantaicraft.entityship.brain.CannonShipBrain;
import com.github.icecheesecat.kantaicraft.entityship.brain.HostileCannonShipBrain;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.entityship.entity.ShipClass;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentClass;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class CannonEntityShip extends EntityShip {

    public static final List<EquipmentClass> EQUITABLE_TYPES = ImmutableList.of(EquipmentClass.SMALL_CANNON);

    public CannonEntityShip(EntityType<? extends PathfinderMob> entityType, ShipClass shipClass, Level level, String name) {
        super(entityType, shipClass, level, EQUITABLE_TYPES, name);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> pDynamic) {
        return !this.isHostileSide() ? CannonShipBrain.makeBrain(this, pDynamic) :
            HostileCannonShipBrain.makeBrain(this, pDynamic);
    }


}
