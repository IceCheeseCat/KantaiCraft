package com.github.icecheesecat.kantaicraft.entity.plane.fighter;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.entity.plane.brain.FighterPlaneAi;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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

    @Override
    public void planeHurtTarget(LivingEntity target, double dmgValue) {
        target.hurt(target.damageSources().mobProjectile(this, target), (float) dmgValue);
    }

}
