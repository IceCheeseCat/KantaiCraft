package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class OwnerHurtTarget extends Behavior<EntityShip> {
    LivingEntity ownerAttackedTarget;
    LivingEntity entityOwner;

    public OwnerHurtTarget() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.REGISTERED));
    }

    @Override
    protected void start(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        pEntity.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, this.ownerAttackedTarget);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        this.entityOwner = pOwner.getOwnerEntity();
        if (this.entityOwner == null) return false;

        this.ownerAttackedTarget = this.entityOwner.getLastHurtMob();
        if (this.ownerAttackedTarget == null) return false;
        if (!pOwner.canAttack(this.ownerAttackedTarget)) return false;
        if (pOwner.isSitDown()) return false;

        return true;
    }
}
