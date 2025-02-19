package com.github.icecheesecat.kantaicraft.block.componentUtil;

import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;

public class ComponentPattern {

    Block[][][] pattern;

    /**
     * return values: 0 (fail), 1(success), 2(skipped)
     */

    public boolean findPattern(Level level, BlockPos blockPos) {

        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[0].length; j++) {
                for (int k = 0; k < pattern[0][0].length; k++) {
                    if (pattern[i][j][k].equals(level.getBlockState(blockPos).getBlock())) {
                        boolean[][][] visited = new boolean[pattern.length][pattern[0].length][pattern[0][0].length];
                        for (var rotation: Rotation.values()) {
                            BlockPos relative = new BlockPos(i, j, k);
                            int result = dfs(visited, level, relative, blockPos, i, j, k, rotation);
                            if (result == 1) return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private int dfs(boolean[][][] visited, Level level, BlockPos start, BlockPos origin, int x, int y, int z, Rotation rotation) {
        if (x < 0 || x >= pattern.length) return 2;
        if (y < 0 || y >= pattern[0].length) return 2;
        if (z < 0 || z >= pattern[0][0].length) return 2;
        if (visited[x][y][z]) return 2;

        visited[x][y][z] = true;
        BlockPos rotPos = new BlockPos(x, y, z);
            rotPos.rotate(rotation);

        if (!pattern[x][y][z].equals(level.getBlockState(origin.offset()).getBlock())) {
            return 0;
        }

        for (var dir: Direction.values()) {
            BlockPos respectToRotatedLevelPos = null;

            if (dfs(visited, level, respectToRotatedLevelPos, origin, x + dir.getStepX(), y + dir.getStepY(), z + dir.getStepZ(), rotation) == 0) {
                return 0;
            }
        }

        return 1;
    }

    public static final class ComponentPatternBuilder {
        private Block[][][] pattern;

        private ComponentPatternBuilder(int row, int height, int column) {
            this.pattern = new Block[row][height][column];
        }

        public static ComponentPatternBuilder start(int row, int height, int column, PatternType type) {
            return new ComponentPatternBuilder(row, height, column);
        }

        public ComponentPatternBuilder addBlock(int x, int y, int z, Block block) {
            this.pattern[x][y][z] = block;
            return this;
        }

        public ComponentPattern build() {
            ComponentPattern componentPattern = new ComponentPattern();
            for (int i = 0; i < pattern.length; i++) {
                for (int j = 0; j < pattern[0].length; j++) {
                    for (int k = 0; k < pattern[0][0].length; k++) {
                        if (pattern[i][j][k] == null) this.pattern[i][j][k] = Blocks.AIR;
                    }
                }
            }
            componentPattern.pattern = this.pattern;
            return componentPattern;
        }
    }
}
