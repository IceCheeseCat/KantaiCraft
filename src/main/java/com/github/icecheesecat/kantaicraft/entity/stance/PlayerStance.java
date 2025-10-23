package com.github.icecheesecat.kantaicraft.entity.stance;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.entity.ship.ShipLeveling;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PlayerStance extends EntityStance {


    public PlayerStance() {
        super(Position.PLAYER);
    }

    @Override
    public void setupSyncedData(SynchedEntityData entityData, RandomSource random) {
        entityData.define(EntityShip.DATA_AIRCRAFT, 0);
        entityData.define(EntityShip.DATA_FUEL, 100.0f);
        entityData.define(EntityShip.DATA_AMMO, 0.0f);
        entityData.define(EntityShip.DATA_SHIP_LEVEL, ShipLeveling.levelZero());
    }

    @Override
    public boolean isHostileToPlayer(Player player) {
        return false;
    }

    @Override
    public boolean isHostileToLiving(LivingEntity livingEntity) {
        return false;
    }
}
