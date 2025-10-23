package com.github.icecheesecat.kantaicraft.entity.stance;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public abstract class EntityStance {

    Side side;

    public EntityStance(Side side) {
        this.side = side;
    }

    public abstract void setupSyncedData(SynchedEntityData entityData, RandomSource random);
    public abstract boolean isHostileToPlayer(Player player);
    public abstract boolean isHostileToLiving(LivingEntity livingEntity);
    public Side getSide() {
        return side;
    }

    public boolean isPlayerSide() {
        return this.side == Side.PLAYER;
    }

    public boolean isHostileSide() {
        return this.side == Side.HOSTILE;
    }

    public boolean isNeutral() {
        return this.side == Side.NEUTRAL;
    }
}

