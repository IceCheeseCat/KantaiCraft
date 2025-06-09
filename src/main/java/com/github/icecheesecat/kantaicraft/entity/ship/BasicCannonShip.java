package com.github.icecheesecat.kantaicraft.entity.ship;

import com.github.icecheesecat.kantaicraft.brain.ship.CannonShipBrain;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class BasicCannonShip extends BasicEntityShip {

    private static final EntityDataSerializer<CannonFireMode> CANNON_FIRE_MODE_ENTITY_DATA_SERIALIZER = EntityDataSerializer.simpleEnum(CannonFireMode.class);
    public static final EntityDataAccessor<CannonFireMode> CANNON_FIRE_MODE = SynchedEntityData.defineId(BasicCannonShip.class, CANNON_FIRE_MODE_ENTITY_DATA_SERIALIZER);
    public static final List<EquipmentType> ATTACKABLE_TYPES = ImmutableList.of(EquipmentType.SMALL_CANNON);

    public BasicCannonShip(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level, ATTACKABLE_TYPES);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        EntityDataSerializers.registerSerializer(CANNON_FIRE_MODE_ENTITY_DATA_SERIALIZER);
        this.entityData.define(CANNON_FIRE_MODE, CannonFireMode.ROUND_ROBIN);
    }

    public CannonFireMode getCannonFireMode() {
        return this.entityData.get(CANNON_FIRE_MODE);
    }

    public void setCannonFireMode(CannonFireMode mode) {
        this.entityData.set(CANNON_FIRE_MODE, mode);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> pDynamic) {
        return CannonShipBrain.makeBrain(this, pDynamic);
    }
}
