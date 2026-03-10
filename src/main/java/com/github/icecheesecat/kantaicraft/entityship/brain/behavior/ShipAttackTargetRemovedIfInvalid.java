package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;


/**
 * Ship will stop attacking when: <br>
 * 1. Target is dead (v) <br>
 * 2. Ship has lost its sight for a while (v) <br>
 */
public class ShipAttackTargetRemovedIfInvalid extends Behavior<EntityShip> {
    private static final long CANT_SEE_DURATION = 200L;
    public ShipAttackTargetRemovedIfInvalid() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.REGISTERED,
                ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get(), MemoryStatus.REGISTERED,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED,
                MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED
        ), Integer.MAX_VALUE);
    }

    private static boolean isTiredOfTryingToReachTarget(LivingEntity pEntity, Optional<Long> pTimeSinceInvalidTarget) {
        return pTimeSinceInvalidTarget.isPresent() && pEntity.level().getGameTime() - pTimeSinceInvalidTarget.get() > 200L;
    }

    /**
     * When can see attack target update last seen pos, otherwise run to the last seen pos. <br>
     * If it hasn't seen the attack target for a while, remove the attack target.
     */
    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        var optional = pEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (optional.isEmpty()) return false;
        LivingEntity target = optional.get();

        boolean canAttack = pEntity.canAttack(target);
        boolean targetAlive = target.isAlive();
        boolean sameLevel = pEntity.level() == target.level();
        boolean tiredOfAttack = pEntity.canRangeAttack() && !pEntity.forceMelee() ?
                !hasLostTargetSightForAWhile(pEntity, target) :
                !isTiredOfTryingToReachTarget(pEntity, pEntity.getBrain().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE));

        return canAttack && targetAlive &&  sameLevel && tiredOfAttack;
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        pEntity.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        pEntity.getBrain().eraseMemory(ModMemoryModuleType.LAST_SAW_TARGET_POS.get());
        pEntity.getBrain().eraseMemory(ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get());
    }

    protected boolean hasLostTargetSightForAWhile(EntityShip entityShip, LivingEntity targetLiving) {

        var optionalVisible = entityShip.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        if (optionalVisible.isEmpty()) return false;
        var visibleLivings = optionalVisible.get();
        if (visibleLivings.contains(targetLiving)) return false;

        var timeInvalidOptional =  entityShip.getBrain().getMemory(ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get());
        if (timeInvalidOptional.isPresent()) {
            return this.isTriedOfTryingToFindBackTarget(entityShip, timeInvalidOptional.get());
        }
        else {
            entityShip.getBrain().setMemory(ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get(), entityShip.level().getGameTime());
            return false;
        }
    }

    private boolean isTriedOfTryingToFindBackTarget(LivingEntity entity, long timeInvalid) {
        return entity.level().getGameTime() - timeInvalid > CANT_SEE_DURATION;
    }

}
