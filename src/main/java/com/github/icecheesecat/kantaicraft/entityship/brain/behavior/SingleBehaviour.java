package com.github.icecheesecat.kantaicraft.entityship.brain.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

/**
 * Do once and will go to cooldown behaviour
 * {@link SingleBehaviour#SingleBehaviour(Map, int)}
 */
public abstract class SingleBehaviour<T extends Mob> extends Behavior<T> {

    int cooldown;
    int maxCooldown;

    /**
     * @param maxCooldown of the next cycle
     */
    public SingleBehaviour(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int maxCooldown) {
        super(pEntryCondition);
        this.maxCooldown = maxCooldown;
    }

    public void setMaxCooldown(int maxCooldown) {
        this.maxCooldown = maxCooldown;
    }

    @Override
    protected void start(ServerLevel pLevel, T pEntity, long pGameTime) {
        singlePerform(pLevel, pEntity, pGameTime);
        this.cooldown = this.maxCooldown;
    }

    protected abstract void singlePerform(ServerLevel pLevel, T pEntity, long pGameTime);

    @Override
    protected final void tick(ServerLevel pLevel, T pOwner, long pGameTime) {
        this.cooldown--;
    }

    @Override
    protected final boolean canStillUse(ServerLevel pLevel, T pEntity, long pGameTime) {
        return this.cooldown >= 0;
    }

    @Override
    protected final boolean timedOut(long pGameTime) {
        return false;
    }

}
