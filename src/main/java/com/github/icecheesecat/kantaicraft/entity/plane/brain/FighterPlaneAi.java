package com.github.icecheesecat.kantaicraft.entity.plane.brain;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.entity.plane.EntityFighterPlane;
import com.github.icecheesecat.kantaicraft.init.ModBrain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class FighterPlaneAi {

    public static Brain<?> makeBrain(EntityFighterPlane plane, Dynamic<?> dyn) {

        Brain<?> brain = PlaneAi.makeBrain(plane, dyn);
        initFighterMission(plane, brain);

        return brain;
    }

    private static void initFighterMission(BasicEntityPlane plane, Brain<?> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                ModBrain.MISSION.get(),
                10,
                ImmutableList.of(),
                MemoryModuleType.ATTACK_TARGET);

    }

}
