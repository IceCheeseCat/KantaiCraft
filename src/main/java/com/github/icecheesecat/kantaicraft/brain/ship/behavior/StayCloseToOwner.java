package com.github.icecheesecat.kantaicraft.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class StayCloseToOwner extends Behavior<BasicEntityShip> {

    private Entity ownerEntity;
    int tickRecalcPath;
    private static final int TICKRECALCPATH = 10;
    private double speedModifier;

    public StayCloseToOwner(int duration, double speedModifier) {
        super(ImmutableMap.of(), duration);
        this.speedModifier = speedModifier;
    }

    @Override
    protected void start(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        this.tickRecalcPath = 0;
    }

    @Override
    protected void tick(ServerLevel pLevel, BasicEntityShip pOwner, long pGameTime) {
        if (--tickRecalcPath <= 0) {
            this.tickRecalcPath = TICKRECALCPATH;
            pOwner.getNavigation().moveTo(this.ownerEntity, speedModifier);
        }
    }

    @Override
    protected void stop(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        super.stop(pLevel, pEntity, pGameTime);
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, BasicEntityShip pEntity, long pGameTime) {
        boolean flag = this.ownerEntity.isAlive() && pEntity.distanceTo(this.ownerEntity) > 3.0d;
        boolean hasAttackTarget = pEntity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);

        if (hasAttackTarget) return false;
        else return flag;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, BasicEntityShip pOwner) {
        this.ownerEntity = pLevel.getEntity(pOwner.getOwner());
        if (this.ownerEntity == null) return false;
        if (pOwner.distanceTo(ownerEntity) > pOwner.getAttributeValue(Attributes.FOLLOW_RANGE)) {
            return true;
        }

        return false;
    }
}
