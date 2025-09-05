package com.github.icecheesecat.kantaicraft.entity.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Comparator;
import java.util.Map;

public class GuardModeAttackTargeting extends SingleBehaviour<EntityShip> {
    public GuardModeAttackTargeting() {
        super(ImmutableMap.of(
                ModMemoryModuleType.IS_PLAYER_SHIP.get(), MemoryStatus.REGISTERED,
                ModMemoryModuleType.IS_GUARDING.get(), MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT), 20);
    }

    @Override
    protected void singlePerform(ServerLevel pLevel, EntityShip pEntity, long pGameTime) {
        pEntity.getBrain().getMemory(ModMemoryModuleType.NEARBY_TARGETS.get()).ifPresent( livings ->
                pEntity.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, livings.stream().min(Comparator.comparingDouble(livingEntity -> livingEntity.distanceTo(pEntity))))
        );
        if (pEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            var entity = pEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
            pEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(entity, true));
            pEntity.getNavigation().stop();
        }
    }
}
