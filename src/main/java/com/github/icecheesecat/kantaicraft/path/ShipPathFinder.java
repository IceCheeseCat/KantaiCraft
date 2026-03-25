package com.github.icecheesecat.kantaicraft.path;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ShipPathFinder extends PathFinder {
    private NodeEvaluator nodeEvaluator;
    private final int maxVisitedNode;
    private Target target;

    public ShipPathFinder(NodeEvaluator nodeEvaluator, int num) {
        super(nodeEvaluator, num);
        this.nodeEvaluator = nodeEvaluator;
        this.maxVisitedNode = num;
    }

    @Override
    public @Nullable Path findPath(PathNavigationRegion region, Mob mob, Set<BlockPos> finalPos, float p_77431_, int p_77432_, float p_77433_) {
        this.nodeEvaluator.prepare(region, mob);
        Node startNode = this.nodeEvaluator.getStart();
        if (startNode == null) {
            return null;
        }

        var it = finalPos.iterator();
        List<Target> targets = new ArrayList<>();
        while(it.hasNext()) {
            BlockPos pos = it.next();
            targets.add(nodeEvaluator.getGoal(pos.getX(), pos.getY(), pos.getZ()));
        }

        // bfs calculate nodes
        Path path = null;
        Map<Target, List<Node>> mListNodes = new HashMap<>();
        for (Target t: targets) {
            mListNodes.put(t, this.bfs(startNode, t));
        }

        // generate path
        Optional<Path> optional1 = mListNodes.entrySet().stream().map(
                entry -> new Path(entry.getValue(), entry.getKey().asBlockPos(), true)
                ).min(Comparator.comparingInt(Path::getNodeCount));
        Optional<Path> optional2 = mListNodes.entrySet().stream().map(
                entry -> new Path(entry.getValue(), entry.getKey().asBlockPos(), false)
                ).min(Comparator.comparingDouble(Path::getDistToTarget).thenComparingInt(Path::getNodeCount));

        if (optional1.isPresent()) {
            path = optional1.get();
        }
        else if (optional2.isPresent()) {
            path = optional2.get();
        }

        this.nodeEvaluator.done();
        return path;
    }

    private List<Node> bfs(Node startNode, Target target) {

        Set<BlockPos> visited = new HashSet<>();
        visited.add(startNode.asBlockPos());

        Queue<Node> queue = new LinkedList<>();
        queue.add(startNode);

        Node desired = null;
        double dis = Double.MAX_VALUE;
        while (!queue.isEmpty()) {
            Node ele = queue.poll(); // returns and removes
            Node[] neighbors = new Node[16];
            this.nodeEvaluator.getNeighbors(neighbors, ele);
            for (Node node: neighbors) {
                if (node == null) break;
                if (visited.contains(node.asBlockPos())) continue;

                visited.add(node.asBlockPos());
                node.cameFrom = ele;

                // find closest to the end node
                if (target.asBlockPos().equals(node.asBlockPos())) {
                    desired = node;
                    break;
                }
                else if (target.distanceToSqr(node) < dis) {
                    dis = target.distanceToSqr(node);
                    desired = node;
                }
                queue.add(node);
            }

        }

        List<Node> pathNodes = new ArrayList<>();
        Node spot = desired;
        while(spot != null) {
            pathNodes.add(0, spot);
            spot = spot.cameFrom;
        }

        return pathNodes;

    }

//    private Path constructPath(Node node, BlockPos targetPos, boolean f) {
//
//        List<Node> pathNodes = new ArrayList<>();
//        Node spot = node;
//        while(spot.cameFrom != null) {
//            pathNodes.add(0, spot);
//            spot = spot.cameFrom;
//        }
//
//        return new Path(pathNodes, targetPos, f);
//    }

}
