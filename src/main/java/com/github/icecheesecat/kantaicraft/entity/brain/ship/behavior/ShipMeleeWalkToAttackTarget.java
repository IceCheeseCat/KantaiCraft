package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Map;
import java.util.Optional;

/**
 * {@link EntityShip} walks toward attackable target when {@link EntityShip#forceMelee()} or
 * target is too close {@link ShipMeleeAttack#MELEE_IF_TARGET_TOO_CLOSE}
 */
public class ShipMeleeWalkToAttackTarget extends SingleBehaviour<EntityShip> {

    public ShipMeleeWalkToAttackTarget(int maxCooldown) {
        super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.PATH, MemoryStatus.REGISTERED,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.REGISTERED), maxCooldown);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        LivingEntity target = pOwner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        return pOwner.forceMelee() || !pOwner.canRangeAttack() || pOwner.distanceTo(target) < ShipMeleeAttack.MELEE_IF_TARGET_TOO_CLOSE;
    }

    @Override
    protected void singlePerform(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        LivingEntity attackTarget = pEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        Optional<NearestVisibleLivingEntities> optional = pEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        if (optional.isPresent() && optional.get().contains(attackTarget) && pEntity.isWithinMeleeAttackRange(attackTarget)) {
            pEntity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        } else {
            pEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(attackTarget, true));
            pEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(attackTarget, false), pEntity.getRunSpeedModifier(), 0));
            pEntity.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            pEntity.getBrain().eraseMemory(MemoryModuleType.PATH);
        }
    }

}
