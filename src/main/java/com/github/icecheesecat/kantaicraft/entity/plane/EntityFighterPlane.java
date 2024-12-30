package com.github.icecheesecat.kantaicraft.entity.plane;

import com.github.icecheesecat.kantaicraft.common.CommonEntityData;
import com.github.icecheesecat.kantaicraft.entity.IFaction;
import com.github.icecheesecat.kantaicraft.entity.plane.brain.FighterPlaneAi;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.mojang.serialization.Dynamic;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

public class EntityFighterPlane extends BasicEntityPlane {


    public EntityFighterPlane(EntityType<? extends Mob> p_21683_, Level level) {
        super(p_21683_, level);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FACTION, 0);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dyn) {
        return FighterPlaneAi.makeBrain(this, dyn);
    }
}
