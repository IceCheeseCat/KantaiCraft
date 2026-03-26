package com.github.icecheesecat.kantaicraft.entityship.stance;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.entityship.entity.features.ShipLeveling;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface NeutralStance extends Stance {

    @Override
    default void setupSyncedDataFromStance(SynchedEntityData entityData, RandomSource random) {
        entityData.define(EntityShip.DATA_AIRCRAFT, 0);
        entityData.define(EntityShip.DATA_AMMO, 0.0f);
        entityData.define(EntityShip.DATA_SHIP_LEVEL, ShipLeveling.createRandom(random));
    }

    @Override
    default boolean isHostileToPlayer(Player player) {
        return false;
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
        return false;
    }

    @Override
    default boolean isNeutralSide() {
        return true;
    }
}
