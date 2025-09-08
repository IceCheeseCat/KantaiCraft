package com.github.icecheesecat.kantaicraft.entity.brain.sensor;

import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Set;

public class AttackTargetVisibilitySensor extends Sensor<LivingEntity> {

    public AttackTargetVisibilitySensor() {
        super(1);
    }

    @Override
    protected void doTick(ServerLevel pLevel, LivingEntity pEntity) {
        if (pEntity.getBrain().checkMemory(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT)) {
            pEntity.getBrain().eraseMemory(ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get());
            pEntity.getBrain().eraseMemory(ModMemoryModuleType.LAST_SAW_TARGET_POS.get());
            return;
        }

        var attackTarget = pEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        if (pEntity.hasLineOfSight(attackTarget)) {
            pEntity.getBrain().eraseMemory(ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get());
            pEntity.getBrain().setMemory(ModMemoryModuleType.LAST_SAW_TARGET_POS.get(), attackTarget.blockPosition());
        }
        else if (pEntity.getBrain().checkMemory(ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get(), MemoryStatus.VALUE_ABSENT)) {
            pEntity.getBrain().setMemory(ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get(), pLevel.getGameTime());
        }

        // hurt by mob has its sight
        if (pEntity.getLastHurtByMob() != null) {
            if (pEntity.canAttack(pEntity.getLastHurtByMob())) {
                pEntity.getBrain().setMemory(ModMemoryModuleType.LAST_SAW_TARGET_POS.get(), pEntity.getLastHurtByMob().blockPosition());
            }
        }
    }


    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.ATTACK_TARGET, ModMemoryModuleType.CANT_SEE_TARGET_SINCE.get(), ModMemoryModuleType.LAST_SAW_TARGET_POS.get());
    }
}
