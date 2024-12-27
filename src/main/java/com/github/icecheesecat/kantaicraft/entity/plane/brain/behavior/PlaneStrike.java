package com.github.icecheesecat.kantaicraft.entity.plane.brain.behavior;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.init.ModBrain;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class PlaneStrike extends Behavior<BasicEntityPlane> {

    private final int cooldown;

    public PlaneStrike(int cooldown) {
        super(ImmutableMap.of(ModBrain.OWNERSHIP.get(), MemoryStatus.VALUE_PRESENT,
                ModBrain.ATTACK_TARGET.get(), MemoryStatus.VALUE_PRESENT,
                ModBrain.STRIKE_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT));

        this.cooldown = cooldown;
    }

    @Override
    protected void start(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
//        super.start(serverLevel, basicEntityPlane, gametime);
        basicEntityPlane.getBrain().setMemoryWithExpiry(ModBrain.STRIKE_COOLDOWN.get(), Unit.INSTANCE, );
    }

    @Override
    protected void tick(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {

        Vec3 towards_target, points_on_target;

    }

    @Override
    protected void stop(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
        basicEntityPlane.getBrain().setMemoryWithExpiry(ModBrain.STRIKE_COOLDOWN.get(), Unit.INSTANCE, this.cooldown);
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
