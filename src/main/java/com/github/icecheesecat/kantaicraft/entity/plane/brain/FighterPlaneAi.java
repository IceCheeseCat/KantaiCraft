package com.github.icecheesecat.kantaicraft.entity.plane.brain;

import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.entity.plane.EntityFighterPlane;
import com.github.icecheesecat.kantaicraft.init.ModBrain;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.ai.Brain;

public class FighterPlaneAi {

    public static Brain<?> makeBrain(EntityFighterPlane plane, Dynamic<?> dyn) {

        Brain<?> brain = PlaneAi.makeBrain(plane, dyn);
        initFighterMission(plane, brain);


        return brain;
    }

    private static void initFighterMission(BasicEntityPlane plane, Brain<?> brain) {

        brain.addActivity(
                ModBrain.MISSION.get(), 5,
                ImmutableList.of(

                )
        );

    }

}
