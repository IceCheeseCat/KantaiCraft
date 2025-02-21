package com.github.icecheesecat.kantaicraft.block.basic.componentUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentPattern {

    Node[][][] tempPattern;
    List<Node> nodes = new ArrayList<>();

    private ComponentPattern(int row, int height, int column) {
        tempPattern = new Node[row][height][column];
    }

    public void init() {
        for (int i = 0; i < tempPattern.length; i++) {
            for (int j = 0; j < tempPattern[0].length; j++) {
                for (int k = 0; k < tempPattern[0][0].length; k++) {
                    if (tempPattern[i][j][k] == null) {
                        tempPattern[i][j][k] = new Node(Blocks.AIR);
                    }
                }
            }
        }

        for (int i = 0; i < tempPattern.length; i++) {
            for (int j = 0; j < tempPattern[0].length; j++) {
                for (int k = 0; k < tempPattern[0][0].length; k++) {
                    if (tempPattern[i][j][k] == null) continue;
                    setupInAllDirections(i, j, k);
                    this.nodes.add(tempPattern[i][j][k]);
                }
            }
        }
    }

    private void setupInAllDirections(int i, int j, int k) {
        int x1, y1, z1;
        x1 = i + 1;
        y1 = j;
        z1 = k;
        if (x1 < tempPattern.length && tempPattern[x1][y1][z1] != null) {
            tempPattern[i][j][k].addNeighbor(Direction.EAST, tempPattern[x1][y1][z1]);
        }
        x1 = i;
        y1 = j + 1;
        z1 = k;
        if (y1 < tempPattern[0].length && tempPattern[x1][y1][z1] != null) {
            tempPattern[i][j][k].addNeighbor(Direction.UP, tempPattern[x1][y1][z1]);
        }
        x1 = i;
        y1 = j;
        z1 = k + 1;
        if (z1 < tempPattern[0][0].length && tempPattern[x1][y1][z1] != null) {
            tempPattern[i][j][k].addNeighbor(Direction.NORTH, tempPattern[x1][y1][z1]);
        }
        x1 = i - 1;
        y1 = j;
        z1 = k;
        if (x1 >= 0 && tempPattern[x1][y1][z1] != null) {
            tempPattern[i][j][k].addNeighbor(Direction.WEST, tempPattern[x1][y1][z1]);
        }
        x1 = i;
        y1 = j - 1;
        z1 = k;
        if (y1 >= 0 && tempPattern[x1][y1][z1] != null) {
            tempPattern[i][j][k].addNeighbor(Direction.DOWN, tempPattern[x1][y1][z1]);
        }
        x1 = i;
        y1 = j;
        z1 = k - 1;
        if (z1 >= 0 && tempPattern[x1][y1][z1] != null) {
            tempPattern[i][j][k].addNeighbor(Direction.SOUTH, tempPattern[x1][y1][z1]);
        }
    }

    public List<BlockPos> findPattern(Level level, BlockPos blockPos) {

        // find node to start
        for (var node: nodes) {
            if (node.block.equals(level.getBlockState(blockPos).getBlock())) {
                for (int mode = 0; mode < 4; mode++) {
                    List<BlockPos> list = traverseNode(node, level, blockPos, mode);
                    this.resetVisited();
                    if (list != null) {
                        return list;
                    }

                }
            }
        }

        return null;
    }

    /**
     * @param mode 0(None), 1(Clockwise), 2(opposite), 3(counterClockWise)
     */
    private List<BlockPos> traverseNode(Node node, Level level, BlockPos pos, int mode) {
        if (node.visited) return null;
        node.visited = true;

        List<BlockPos> ret = new ArrayList<>();
        if (!level.getBlockState(pos).getBlock().equals(node.block)) {
//            System.out.println("Not equal at " + pos + " tpye:" + level.getBlockState(pos).getBlock());
            return null;
        }
        else {
            ret.add(pos);
        }

        // end node check
        if (node.neighbors.entrySet().stream().allMatch(entry -> entry.getValue().visited)) {
            return ret;
        }

        // all neighbors' return value must not be null
        for (var neiNode: node.neighbors.entrySet()) {
            if (neiNode.getValue().visited) continue;

            Direction modeDirection = modeSwitchDirection(neiNode.getKey(), mode);
            BlockPos modePos = pos.relative(modeDirection);
            List<BlockPos> list = traverseNode(neiNode.getValue(), level, modePos, mode);
            if (list == null) {
                return null;
            }
            else {
                ret.addAll(list);
            }
        }

        return ret;
    }

    private void resetVisited() {
        for (var node: nodes) {
            node.visited = false;
        }
    }

    private Direction modeSwitchDirection(Direction direction,  int mode) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return direction;
        }
        return switch (mode) {
            case 0 -> direction;
            case 1 -> direction.getClockWise();
            case 2 -> direction.getOpposite();
            case 3 -> direction.getCounterClockWise();
            default -> throw new IllegalStateException("Mode cannot be " + mode);
        };
    }

    public static class Node {
        Block block;
        Map<Direction, Node> neighbors = new HashMap<>();
        boolean visited;

        private Node(Block block) {
            this.block = block;
        }

        private Node addNeighbor(Direction direction, Node node) {
            neighbors.put(direction, node);
            return this;
        }

    }

    public enum Mode {
        ROTATE_AROUND_Y,
        ROTATE_AROUND_X,

    }

    public static final class Builder {
        private Node[][][] tempPattern;
        int row, height, column;

        private Builder(int r, int h, int c) {
            this.row = r;
            this.height = h;
            this.column = c;
            this.tempPattern = new Node[r][h][c];
        }

        public static Builder start(int r, int h, int c) {
            return new Builder(r, h, c);
        }

        public Builder addBlock(int x, int y, int z, Block block) {
            this.tempPattern[x][y][z] = new Node(block);
            return this;
        }
        public ComponentPattern build() {
            ComponentPattern componentPattern = new ComponentPattern(row, height, column);
            componentPattern.tempPattern = this.tempPattern;
            componentPattern.init();
            for (var node: componentPattern.nodes) {
                System.out.println(node.block + ", ");
                for (var nei: node.neighbors.entrySet()) {
                    System.out.println("---" + nei.getKey() + " " + nei.getValue().block);
                }
                System.out.println();
            }
            return componentPattern;
        }
    }
}
