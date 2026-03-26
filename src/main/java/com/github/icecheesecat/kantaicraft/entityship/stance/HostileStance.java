package com.github.icecheesecat.kantaicraft.entityship.stance;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.entityship.entity.features.ShipLeveling;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface HostileStance extends Stance {

    @Override
    default void setupSyncedDataFromStance(SynchedEntityData entityData, RandomSource random) {
        entityData.define(EntityShip.DATA_AIRCRAFT, Integer.MAX_VALUE);
        entityData.define(EntityShip.DATA_AMMO, Float.MAX_VALUE);
        entityData.define(EntityShip.DATA_SHIP_LEVEL, ShipLeveling.createRandom(random));
        entityData.define(EntityShip.DATA_SHOULD_PICK_UP_ITEM, false);
    }

    @Override
    default boolean isHostileToPlayer(Player player) {
        return true;
    }

    @Override
    default boolean isHostileToLiving(LivingEntity livingEntity) {
        return false;
    }

    @Override
    default boolean isPlayerSide() {
        return false;
    }

    @Override
    default boolean isHostileSide() {
        return true;
    }

    @Override
    default boolean isNeutralSide() {
        return false;
    }

    EntityType<?> getPlayerSideEntityType();
}
