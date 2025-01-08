package com.github.icecheesecat.kantaicraft.path;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShipPathFinder extends PathFinder {
    private NodeEvaluator nodeEvaluator;
    private final int maxVisitedNode;

    public ShipPathFinder(NodeEvaluator nodeEvaluator, int num) {
        super(nodeEvaluator, num);
        this.nodeEvaluator = nodeEvaluator;
        this.maxVisitedNode = num;
    }

    @Override
    public @Nullable Path findPath(PathNavigationRegion region, Mob mob, Set<BlockPos> finalPos, float p_77431_, int p_77432_, float p_77433_) {
        this.nodeEvaluator.prepare(region, mob);
        var it = finalPos.iterator();
        List<Target> targets = new ArrayList<>();
        while(it.hasNext()) {
            BlockPos pos = it.next();
            targets.add(nodeEvaluator.getGoal(pos.getX(), pos.getY(), pos.getZ()));
        }


    }
}
