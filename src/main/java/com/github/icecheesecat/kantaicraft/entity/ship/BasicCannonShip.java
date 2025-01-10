package com.github.icecheesecat.kantaicraft.entity.ship;

import com.github.icecheesecat.kantaicraft.brain.ship.CannonShipBrain;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.mojang.serialization.Dynamic;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

public abstract class BasicCannonShip extends BasicEntityShip {

    private static final EntityDataAccessor<CannonFireMode> CANNON_FIRE_MODE = SynchedEntityData.defineId(BasicCannonShip.class, EntityDataSerializer.simpleEnum(CannonFireMode.class));

    protected BasicCannonShip(EntityType<? extends PathfinderMob> entityType, Level level, EquipmentSlots equipmentSlot) {
        super(entityType, level, equipmentSlot);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.set(CANNON_FIRE_MODE, CannonFireMode.ROUND_ROBIN);
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
