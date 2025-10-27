package com.github.icecheesecat.kantaicraft.entityship.stance;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface Stance {

    void setupSyncedDataFromStance(SynchedEntityData entityData, RandomSource random);
    boolean isHostileToPlayer(Player player);
    boolean isHostileToLiving(LivingEntity livingEntity);

    boolean isPlayerSide();

    boolean isHostileSide();

    boolean isNeutralSide();
}

