package com.github.icecheesecat.kantaicraft.entity.stance;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.entity.ship.ShipLeveling;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class HostileStance extends EntityStance {


    public HostileStance() {
        super(Position.HOSTILE);
    }

    @Override
    public void setupSyncedData(SynchedEntityData entityData, RandomSource random) {
        entityData.define(EntityShip.DATA_AIRCRAFT, Integer.MAX_VALUE);
        entityData.define(EntityShip.DATA_FUEL, Float.MAX_VALUE);
        entityData.define(EntityShip.DATA_AMMO, Float.MAX_VALUE);
        entityData.define(EntityShip.DATA_SHIP_LEVEL, ShipLeveling.createRandom(random));
    }

    @Override
    public boolean isHostileToPlayer(Player player) {
        return true;
    }

    @Override
    public boolean isHostileToLiving(LivingEntity livingEntity) {
        return false;
    }
}
