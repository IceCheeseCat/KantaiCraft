package com.github.icecheesecat.kantaicraft.entity.plane;

import com.github.icecheesecat.kantaicraft.init.ModBrainActivity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class PlaneCircle extends Behavior<BasicEntityPlane> {

    private static final int DURATION = 60 * 20;
    private static final int ATTACK_COOLDOWN = 100;

    public PlaneCircle() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT),
                DURATION);
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
        return true;
    }

    @Override
    protected void start(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
//        basicEntityPlane.getBrain().setMemoryWithExpiry(ModBrainActivity.PLANE_TIMEOUT.get(), true, );
//        basicEntityPlane.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, );
    }

    @Override
    protected void tick(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {

    }

    @Override
    protected void stop(ServerLevel serverLevel, BasicEntityPlane basicEntityPlane, long gametime) {
        super.stop(serverLevel, basicEntityPlane, gametime);
    }

    @Override
    protected boolean timedOut(long gametime) {
        return super.timedOut(gametime);
    }
}
