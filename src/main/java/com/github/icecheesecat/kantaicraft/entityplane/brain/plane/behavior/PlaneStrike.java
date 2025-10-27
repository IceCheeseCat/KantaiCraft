package com.github.icecheesecat.kantaicraft.entityplane.brain.plane.behavior;

import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.entityplane.BasicEntityPlane;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

public class PlaneStrike extends Behavior<BasicEntityPlane> {

    private final int cooldown;

    public PlaneStrike(int cooldown) {
        super(ImmutableMap.of(ModMemoryModuleType.OWNERSHIP.get(), MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.STRIKE_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT));

        this.cooldown = cooldown;
    }

    @Override
    protected void start(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
//        super.start(serverLevel, basicEntityPlane, gametime);
        basicEntityPlane.getBrain().setMemoryWithExpiry(ModMemoryModuleType.STRIKE_COOLDOWN.get(), Unit.INSTANCE, 60);
    }

    @Override
    protected void tick(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {

        Vec3 towards_target, points_on_target;

    }

    @Override
    protected void stop(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
        basicEntityPlane.getBrain().setMemoryWithExpiry(ModMemoryModuleType.STRIKE_COOLDOWN.get(), Unit.INSTANCE, this.cooldown);
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
        return true;
    }

    @Override
    protected boolean timedOut(long p_22537_) {
        return false;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane) {
        return super.checkExtraStartConditions(serverLevel, basicEntityPlane);
    }
}
