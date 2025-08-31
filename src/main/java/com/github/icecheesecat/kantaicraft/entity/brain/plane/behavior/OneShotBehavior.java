package com.github.icecheesecat.kantaicraft.entity.brain.plane.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public abstract class OneShotBehavior<E extends LivingEntity> extends Behavior<E> {

    public OneShotBehavior(Map<MemoryModuleType<?>, MemoryStatus> p_22528_) {
        super(p_22528_);
    }

    @Override
    protected final void start(ServerLevel serverLevel, E livingEntity, long gametime) {
        if (trigger(serverLevel, livingEntity, gametime)) {
            System.out.println("One Shot Behavior " + this.debugString());
        }
    }

    @Override
    protected final boolean canStillUse(ServerLevel p_22545_, E p_22546_, long p_22547_) {
        return false;
    }

    @Override
    protected final boolean timedOut(long p_22537_) {
        return false;
    }

    @Override
    protected final boolean checkExtraStartConditions(ServerLevel p_22538_, E p_22539_) {
        return true;
    }

    public abstract boolean trigger(ServerLevel serverLevel, E livingEntity, long gametime);

    @Override
    protected final void tick(ServerLevel p_22551_, E p_22552_, long p_22553_) {
    }

    @Override
    protected final void stop(ServerLevel p_22548_, E p_22549_, long p_22550_) {
    }

    @Override
    protected final boolean hasRequiredMemories(E p_22544_) {
        return super.hasRequiredMemories(p_22544_);
    }
}
