package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;

public class ActivityReturnCountdown extends Behavior<LivingEntity> {

    final Activity activity;
    final TriPredicate pause;
    final TriPredicate reset;
    final int resetTime;
    final TriConsumer action;
    private final MemoryModuleType<Integer> targetMemory;

    public ActivityReturnCountdown(MemoryModuleType<Integer> memoryModuleType, MemoryStatus status, Activity activity, TriPredicate pause, TriPredicate reset, int resetTime, TriConsumer action) {
        super(ImmutableMap.of(memoryModuleType, status));
        this.targetMemory = memoryModuleType;
        this.activity = activity;
        this.pause = pause;
        this.reset = reset;
        this.resetTime = resetTime;
        this.action = action;
    }

    @Override
    protected void tick(ServerLevel pLevel, LivingEntity pOwner, long pGameTime) {
        if (reset.test(pLevel, pOwner, pGameTime)) {
            pOwner.getBrain().setMemory(targetMemory, resetTime);
        }
        else if (!pause.test(pLevel, pOwner, pGameTime)) {
            Optional<Integer> optional = pOwner.getBrain().getMemory(targetMemory);
            pOwner.getBrain().setMemory(targetMemory, optional.get() - 1);
        }
    }

    @Override
    protected void stop(ServerLevel pLevel, LivingEntity pEntity, long pGameTime) {
        pEntity.getBrain().eraseMemory(targetMemory);
        pEntity.getBrain().setActiveActivityIfPossible(this.activity);
        this.action.accept(pLevel, pEntity, pGameTime);
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, LivingEntity pEntity, long pGameTime) {
        return pEntity.getBrain().hasMemoryValue(targetMemory) && pEntity.getBrain().getMemory(targetMemory).get() > 0;
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }

    @FunctionalInterface
    public interface TriPredicate {
        boolean test(ServerLevel pLevel, LivingEntity pEntity, long pGameTime);
    }

    @FunctionalInterface
    public interface TriConsumer {
        void accept(ServerLevel pLevel, LivingEntity pEntity, long pGameTime);
    }

}
