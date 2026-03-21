package com.github.icecheesecat.kantaicraft.block.facilities.pattern;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class FindPatternResult {
    final List<BlockPos> blockPoses = new ArrayList<>();
    Direction direction;
    BlockPos core = null;
    ResultCode code = ResultCode.NORMAL;
    BlockPos start, end;

    private FindPatternResult() {
    }

    public FindPatternResult(ResultCode code) {
        this.code = code;
    }

    public FindPatternResult(BlockPos blockPos, ResultCode code) {
        this.blockPoses.add(blockPos);
        this.core = null;
        this.code = code;
    }

    public static FindPatternResult fail() {
        return new FindPatternResult(ResultCode.FAILED);
    }

    public static FindPatternResult visited() {
        return new FindPatternResult(ResultCode.VISITED);
    }

    public static FindPatternResult pass() {
        return new FindPatternResult(ResultCode.PASS);
    }

    public static FindPatternResult success(BlockPos blockPos) {
        return new FindPatternResult(blockPos, ResultCode.NORMAL);
    }

    public boolean isSuccess() {
        return this.code == ResultCode.NORMAL;
    }

    public void merge(FindPatternResult other) {
        this.blockPoses.addAll(other.blockPoses);
        this.code = this.code.or(other.code);
        if (this.core != null && other.core != null) {
            this.code = ResultCode.MORE_THAN_TWO_CORES;
        } else {
            if (other.core != null) {
                this.core = other.core;
            }
        }
    }

    public List<BlockPos> getBlockPoses() {
        return blockPoses;
    }

    public BlockPos getCore() {
        return core;
    }

    public void setCore(BlockPos core) {
        this.core = core;
    }

    public ResultCode getCode() {
        return code;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setStartAndEnd(BlockPos start, BlockPos end) {
        this.start = start;
        this.end = end;
    }

    public BlockPos getStart() {
        return start;
    }

    public BlockPos getEnd() {
        return end;
    }

    public enum ResultCode {
        NORMAL(false),
        PASS(false),
        VISITED(false),
        MORE_THAN_TWO_CORES(true),
        NO_CORE(true),
        FAILED(true);

        private final boolean isFailure;

        ResultCode(boolean isFailure) {
            this.isFailure = isFailure;
        }

        public ResultCode or(ResultCode other) {
            if (this.isFailure || other.isFailure) {
                return FAILED;
            }

            return NORMAL;
        }
    }
}
