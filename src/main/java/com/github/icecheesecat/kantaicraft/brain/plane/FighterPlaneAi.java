package com.github.icecheesecat.kantaicraft.brain.plane;

import com.github.icecheesecat.kantaicraft.customObjects.ModActitvity;
import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.entity.plane.fighter.EntityFighterPlane;
import com.github.icecheesecat.kantaicraft.brain.plane.behavior.FighterPlaneAttack;
import com.github.icecheesecat.kantaicraft.brain.plane.behavior.PlaneFollowTarget;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class FighterPlaneAi {

    public static Brain<?> makeBrain(EntityFighterPlane plane, Dynamic<?> dyn) {

        Brain<BasicEntityPlane> brain = PlaneAi.makeBrain(plane, dyn);
        initFighterMission(plane, brain);

        return brain;
    }

    private static void initFighterMission(BasicEntityPlane plane, Brain<BasicEntityPlane> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                ModActitvity.MISSION.get(),
                10,
                ImmutableList.of(new FighterPlaneAttack(),
                        new PlaneFollowTarget(plane.getPlaneAttributes().getFlySpeed(), plane.getPlaneAttributes().getTurnAcceleration()),
                        EraseMemoryIf.create(FighterPlaneAi::targetIsInvalid, MemoryModuleType.ATTACK_TARGET)),
                MemoryModuleType.ATTACK_TARGET);

    }

    private static boolean targetIsInvalid(LivingEntity target) {
        if (target == null) return true;
        return !target.isAlive();
    }

}
