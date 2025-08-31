package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * {@link EntityShip} walks toward attackable target when {@link EntityShip#canMelee()} or
 * target is too close {@link ShipMeleeAttack#MELEE_IF_TARGET_TOO_CLOSE}
 */
public class ShipWalkToAttackTarget extends SingleBehaviour<EntityShip> {

    public ShipWalkToAttackTarget(int maxCooldown) {
        super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.PATH, MemoryStatus.REGISTERED,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.REGISTERED), maxCooldown);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        LivingEntity target = pOwner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        return pOwner.canMelee() || !pOwner.hasEnoughAmmo() || pOwner.distanceTo(target) < ShipMeleeAttack.MELEE_IF_TARGET_TOO_CLOSE;
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
            pEntity.getBrain().eraseMemory(MemoryModuleType.PATH);
        }
    }

}
