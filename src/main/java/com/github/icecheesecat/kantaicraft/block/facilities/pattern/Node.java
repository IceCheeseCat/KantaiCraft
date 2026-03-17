package com.github.icecheesecat.kantaicraft.block.facilities.pattern;

import com.github.icecheesecat.kantaicraft.block.facilities.FacilityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;

public class Node {
    private final FacilityBlock block;
    private BlockPos offset;

    public Node(FacilityBlock block, int i, int j, int k) {
        this.block = block;
        this.offset = new BlockPos(i, j, k);
    }

    public void rotate(Rotation rotation) {
        this.offset = this.offset.rotate(rotation);
    }

    public void flip() {
        this.offset = new BlockPos(-this.offset.getX(), this.offset.getY(), this.offset.getZ());
    }

    public FacilityBlock getBlock() {
        return block;
    }

    public BlockPos getOffset() {
        return offset;
    }

    public Node clone() {
        return new Node(this.block, this.offset.getX(), this.offset.getY(), this.offset.getZ());
    }
}
