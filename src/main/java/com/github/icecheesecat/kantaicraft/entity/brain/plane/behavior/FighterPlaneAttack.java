package com.github.icecheesecat.kantaicraft.entity.brain.plane.behavior;

import com.github.icecheesecat.kantaicraft.common.BoundingBoxAlgorithm;
import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class FighterPlaneAttack extends OneShotBehavior<BasicEntityPlane> {

    public FighterPlaneAttack() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    public boolean trigger(ServerLevel serverLevel, BasicEntityPlane entityFighterPlane, long gametime) {
//        if (en (entityFighterPlane.getLookAngle()))
        var optionalTarget = entityFighterPlane.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (optionalTarget.isPresent()) {
            LivingEntity target = optionalTarget.get();
            if (BoundingBoxAlgorithm.rayIntersection(target.getBoundingBox(), target.getEyePosition(), target.getLookAngle()) && entityFighterPlane.distanceTo(target) < 20.0d) {

                entityFighterPlane.planeHurtTarget(target, entityFighterPlane.getPlaneAttributes().getAntiAir());
                entityFighterPlane.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, 20);
                return true;

            }

        }

        return false;
    }

}
