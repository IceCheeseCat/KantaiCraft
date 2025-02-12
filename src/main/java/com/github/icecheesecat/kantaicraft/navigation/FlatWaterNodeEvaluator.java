package com.github.icecheesecat.kantaicraft.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.*;

public class FlatWaterNodeEvaluator extends WalkNodeEvaluator {

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter pLevel, int pX, int pY, int pZ) {
        var pathType = super.getBlockPathType(pLevel, pX, pY, pZ);
        if (pathType == BlockPathTypes.WATER) {
            return BlockPathTypes.WALKABLE;
        }

        return pathType;
    }

}
