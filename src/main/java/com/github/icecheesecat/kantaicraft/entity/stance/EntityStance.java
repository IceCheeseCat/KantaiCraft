package com.github.icecheesecat.kantaicraft.entity.stance;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public abstract class EntityStance {

    Position position;

    public EntityStance(Position position) {
        this.position = position;
    }

    public abstract void setupSyncedData(SynchedEntityData entityData, RandomSource random);
    public abstract boolean isHostileToPlayer(Player player);
    public abstract boolean isHostileToLiving(LivingEntity livingEntity);

}

