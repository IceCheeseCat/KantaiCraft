package com.github.icecheesecat.kantaicraft.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

public class ShipMeleeAttack {

    public static OneShot<BasicEntityShip> create(int pCooldownBetweenAttacks) {
        return BehaviorBuilder.create((selfEntity) -> {
            return selfEntity.group(selfEntity.registered(MemoryModuleType.LOOK_TARGET), selfEntity.present(MemoryModuleType.ATTACK_TARGET), selfEntity.absent(MemoryModuleType.ATTACK_COOLING_DOWN), selfEntity.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(selfEntity, (lookTarget, attackTarget, attackCooldown, nearestVisibleTarget) -> {
                return (serverLevel, shipEntity, p_258541_) -> {
                    LivingEntity livingentity = selfEntity.get(attackTarget);
                    if (selfEntity.<NearestVisibleLivingEntities>get(nearestVisibleTarget).contains(livingentity)) {
                        lookTarget.set(new EntityTracker(livingentity, true));
//                        shipEntity.swing(InteractionHand.MAIN_HAND);
                        shipEntity.doHurtTarget(livingentity);
                        attackCooldown.setWithExpiry(true, (long)pCooldownBetweenAttacks);
                        return true;
                    } else {
                        return false;
                    }
                };
            });
        });
    }

}
