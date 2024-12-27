package com.github.icecheesecat.kantaicraft.entity.plane;

import com.github.icecheesecat.kantaicraft.entity.plane.brain.FighterPlaneAi;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

public class EntityFighterPlane extends BasicEntityPlane {


    public EntityFighterPlane(EntityType<? extends Mob> p_21683_, Level level) {
        super(p_21683_, level);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dyn) {
        return FighterPlaneAi.makeBrain(this, dyn);
    }
}
