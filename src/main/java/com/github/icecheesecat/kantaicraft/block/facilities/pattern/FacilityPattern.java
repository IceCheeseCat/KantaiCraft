package com.github.icecheesecat.kantaicraft.block.facilities.pattern;

import com.github.icecheesecat.kantaicraft.block.facilities.FacilityBlock;
import com.github.icecheesecat.kantaicraft.block.facilities.FacilityCoreBlock;
import com.github.icecheesecat.kantaicraft.exception.KantaiCraftException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FacilityPattern {

    private Component name;
    private final int length, width, height;
    private Node[][][] templatePattern;
    private List<PatternRotation> allPossiblePatterns;
    public static final Direction[] DIRECTIONS = {Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH};

    private FacilityPattern(int length, int height, int width, Node[][][] templatePattern, Component name) {
        this.name = name;
        this.length = length;
        this.height = height;
        this.width = width;
        this.templatePattern = templatePattern;
        init();
    }

    public void init() {
        createAllPossibilities();
    }

    private void createAllPossibilities() {
        this.allPossiblePatterns = new ArrayList<>();
        for (var dir: DIRECTIONS) {
            allPossiblePatterns.add(new PatternRotation(this.length, this.height, this.width, dir, cloneTemplate()));
            allPossiblePatterns.add(new PatternRotation(this.length, this.height, this.width, dir, cloneTemplate()));
        }
//        this.allPossiblePatterns.forEach(patternRotation -> {
//            System.out.print(patternRotation.direction + " >> flipped >> " + patternRotation.flip + " >> ");
//            visitArrayElements(patternRotation.offsetNodes, ((i, j, k, node) -> System.out.print(node.getOffset() + " | ")));
//            System.out.println();
//        });
    }

    private Node[][][] cloneTemplate() {
        Node[][][] newArray = new Node[length][height][width];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    newArray[i][j][k] = templatePattern[i][j][k].clone();
                }
            }
        }

        return newArray;
    }

    public @NotNull FindPatternResult findPattern(Level level, BlockPos blockPos) {

        for (var patternRotation: this.allPossiblePatterns) {
            var result = patternRotation.tryFindPattern(level, blockPos);
            if (result.isSuccess() && result.getCore() != null) {
                BlockPos start = blockPos.offset(patternRotation.offsetNodes[0][0][0].getOffset());
                BlockPos end = blockPos.offset(patternRotation.offsetNodes[patternRotation.length - 1][patternRotation.height - 1][patternRotation.width - 1].getOffset());
                result.setStartAndEnd(start, end);
                return result;
            }
        }

        return FindPatternResult.fail();
    }

    public static void visitArrayElements(Node[][][] nodes, NodeAccessor accessor) {
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                for (int k = 0; k < nodes[i][j].length; k++) {
                    accessor.accept(i, j, k, nodes[i][j][k]);
                }
            }
        }
    }

    @FunctionalInterface
    public interface NodeAccessor {
        void accept(int i, int j, int k, Node node);
    }

    public static final class Builder {
        private int length, height, width;
        private Node[][][] templatePattern;
        private Component name;

        private Builder(int length, int height, int width) {
            this.length = length;
            this.height = height;
            this.width = width;
            this.templatePattern = new Node[length][height][width];
        }

        public static Builder start(int length, int height, int width) {
            return new Builder(length, height, width);
        }

        public Builder addBlock(int x, int y, int z, FacilityBlock block) {
            this.templatePattern[x][y][z] = new Node(block, x, y, z);
            return this;
        }

        public Builder addCoreBlock(FacilityCoreBlock block) {
            return this.addBlock(0,0,0, block);
        }

        public Builder setName(Component name) {
            this.name = name;
            return this;
        }

        public Builder addAll(FacilityBlock block) {
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < height; j++) {
                    for (int k = 0; k < width; k++) {
                        this.templatePattern[i][j][k] = new Node(block, i, j, k);
                    }
                }
            }

            return this;
        }

        public FacilityPattern build() {
            if (this.length <= 0) {
                throw new KantaiCraftException(this.getClass(), "pattern length can only be greater than 0, your value = " + this.length);
            }
            if (this.height <= 0) {
                throw new KantaiCraftException(this.getClass(), "pattern height can only be greater than 0, your value = " + this.height);
            }
            if (this.width <= 0) {
                throw new KantaiCraftException(this.getClass(), "pattern width can only be greater than 0, your value = " + this.width);
            }
            return new FacilityPattern(this.length, this.height, this.width, templatePattern, this.name);
        }
    }

    @FunctionalInterface
    public interface Getter {
        FacilityPattern get();
    }
}
