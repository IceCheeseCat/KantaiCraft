package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

import java.util.Map;

public class ShipMeleeAttack extends SingleBehaviour<EntityShip> {

    public static final float MELEE_IF_TARGET_TOO_CLOSE = 3.0f;
    public ShipMeleeAttack(int maxCooldown) {
        super(Map.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT
        ), maxCooldown);
    }

    @Override
    protected void singlePerform(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
//        System.out.println("punch");
        LivingEntity livingentity = pEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        if (pEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get().contains(livingentity)) {
            pEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingentity, true));
            pEntity.doHurtTarget(livingentity);
        }
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityShip pOwner) {
        LivingEntity attackTarget = pOwner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        return pOwner.isWithinMeleeAttackRange(attackTarget);
    }
}
