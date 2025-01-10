package com.github.icecheesecat.kantaicraft.path;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.*;

public class FluidSurfacePathNavigator extends GroundPathNavigation {

    public FluidSurfacePathNavigator(Mob p_26448_, Level p_26449_) {
        super(p_26448_, p_26449_);
    }

    @Override
    protected PathFinder createPathFinder(int num) {
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanOpenDoors(true);
        this.nodeEvaluator.setCanOpenDoors(true);

        return new ShipPathFinder(nodeEvaluator, num);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.mob.isInWater() || this.mob.onGround();
    }

    @Override
    public boolean isStableDestination(BlockPos blockPos) {
        return this.mob.level().getBlockState(blockPos).is(Blocks.WATER) || super.isStableDestination(blockPos);
    }
}
