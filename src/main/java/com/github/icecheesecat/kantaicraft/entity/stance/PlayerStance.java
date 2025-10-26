package com.github.icecheesecat.kantaicraft.entity.stance;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.entity.ship.ShipLeveling;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface PlayerStance extends Stance {

    @Override
    default void setupSyncedDataFromStance(SynchedEntityData entityData, RandomSource random) {
        entityData.define(EntityShip.DATA_AIRCRAFT, 0);
        entityData.define(EntityShip.DATA_FUEL, 100.0f);
        entityData.define(EntityShip.DATA_AMMO, 0.0f);
        entityData.define(EntityShip.DATA_SHIP_LEVEL, ShipLeveling.levelZero());
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
        return true;
    }

    @Override
    default boolean isHostileSide() {
        return false;
    }

    @Override
    default boolean isNeutralSide() {
        return false;
    }
}
