package com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class BlockPosHelper {

    public static CompoundTag writeNbt(BlockPos blockPos) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("pos_x", blockPos.getX());
        nbt.putInt("pos_y", blockPos.getY());
        nbt.putInt("pos_z", blockPos.getZ());
        return nbt;
    }

    public static BlockPos readNbt(CompoundTag nbt) {
        return new BlockPos(nbt.getInt("pos_x"), nbt.getInt("pos_y"), nbt.getInt("pos_z"));
    }

    public static CompoundTag listWriteNbt(List<BlockPos> blockPoses) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("poses_size", blockPoses.size());
        for (int i = 0; i < blockPoses.size(); i++) {
            nbt.put("pos" + i, writeNbt(blockPoses.get(i)));
        }

        return nbt;
    }

    public static List<BlockPos> listReadNbt(CompoundTag nbt) {
        int size = nbt.getInt("poses_size");
        List<BlockPos> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i, readNbt(nbt.getCompound("pos"+i)));
        }

        return list;
    }

}
