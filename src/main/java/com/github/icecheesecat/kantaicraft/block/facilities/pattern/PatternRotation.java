package com.github.icecheesecat.kantaicraft.block.facilities.pattern;

import com.github.icecheesecat.kantaicraft.block.BlockStateProperties;
import com.github.icecheesecat.kantaicraft.block.facilities.FacilityCoreBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import org.jetbrains.annotations.NotNull;

public class PatternRotation {

    final int length, height, width;
    Direction direction;
    Node[][][] offsetNodes;

    public PatternRotation(int length, int height, int width, Direction direction, Node[][][] offsetNodes) {
        this.length = length;
        this.height = height;
        this.width = width;
        this.direction = direction;
        this.offsetNodes = offsetNodes;

        FacilityPattern.visitArrayElements(this.offsetNodes, ((i, j, k, node) -> {

            switch (direction) {
                case EAST:
                    node.rotate(Rotation.NONE);
                    break;
                case WEST:
                    node.rotate(Rotation.CLOCKWISE_180);
                    break;
                case SOUTH:
                    node.rotate(Rotation.CLOCKWISE_90);
                    break;
                case NORTH:
                    node.rotate(Rotation.COUNTERCLOCKWISE_90);
                    break;
            }

        }));

    }

    @NotNull
    public FindPatternResult tryFindPattern(Level level, BlockPos startPos) {
        FindPatternResult result = shiftThanTraverseNodes(level, startPos);
        result.setDirection(this.direction);
        return result;
    }

    @NotNull FindPatternResult shiftThanTraverseNodes(Level level, BlockPos blockPos) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    BlockPos shifted = blockPos.subtract(offsetNodes[i][j][k].getOffset());
                    var result = traverseNodes(new boolean[length][height][width], level, shifted, i, j, k);
                    if (result.isSuccess() && result.getCore() != null) {
                        return result;
                    }
                }
            }
        }

        return FindPatternResult.fail();
    }

    FindPatternResult traverseNodes(boolean[][][] visit, Level level, BlockPos origin, int i, int j, int k) {
        if (i < 0 || i >= length) return FindPatternResult.pass();
        if (j < 0 || j >= height) return FindPatternResult.pass();
        if (k < 0 || k >= width) return FindPatternResult.pass();

        Node node = this.offsetNodes[i][j][k];
        if (visit[i][j][k]) {
            return FindPatternResult.visited();
        }
        visit[i][j][k] = true;
        BlockPos offsetPos = this.offsetNodes[i][j][k].getOffset();
        BlockPos offseted = origin.offset(offsetPos);
        if (level.getBlockState(offseted).hasProperty(BlockStateProperties.WORKING_FACILITY) && level.getBlockState(offseted).getValue(BlockStateProperties.WORKING_FACILITY)) {
            return FindPatternResult.fail();
        }
        if (!level.getBlockState(offseted).is(node.getBlock())) {
            return FindPatternResult.fail();
        }

        FindPatternResult result = FindPatternResult.success(offseted);
        if (level.getBlockState(offseted).getBlock() instanceof FacilityCoreBlock) {
            result.setCore(offseted);
        }

        for (var dir : Direction.values()) {
            var result1 = traverseNodes(visit, level, origin, i + dir.getStepX(), j + dir.getStepY(), k + dir.getStepZ());
            result.merge(result1);
        }

        return result;
    }

    private boolean canContinueSearch(FindPatternResult.ResultCode code) {
        return code == FindPatternResult.ResultCode.PASS || code == FindPatternResult.ResultCode.NORMAL || code == FindPatternResult.ResultCode.VISITED;
    }

}
