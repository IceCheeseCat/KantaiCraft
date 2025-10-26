package com.github.icecheesecat.kantaicraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public final class CompoundTagHelper {

    public static CompoundTag putVec3(Vec3 vec3, String name) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble(name + ".x", vec3.x);
        nbt.putDouble(name + ".y", vec3.y);
        nbt.putDouble(name + ".z", vec3.z);

        return nbt;
    }
    
    public static Vec3 getVec3(CompoundTag nbt, String name) {
        double x = 0, y = 0, z = 0;
        if (nbt.contains(name + ".x")) {
            x = nbt.getDouble(name + ".x");
        }
        if (nbt.contains(name + ".y")) {
            y = nbt.getDouble(name + ".y");
        }
        if (nbt.contains(name + ".z")) {
            z = nbt.getDouble(name + ".z");
        }
        
        return new Vec3(x, y, z);
    }

    public static CompoundTag putBlockPos(BlockPos blockPos) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("pos_x", blockPos.getX());
        nbt.putInt("pos_y", blockPos.getY());
        nbt.putInt("pos_z", blockPos.getZ());
        return nbt;
    }

    public static BlockPos getBlockPos(CompoundTag nbt) {
        return new BlockPos(nbt.getInt("pos_x"), nbt.getInt("pos_y"), nbt.getInt("pos_z"));
    }

    public static CompoundTag putListOfBlockPos(List<BlockPos> blockPoses) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("poses_size", blockPoses.size());
        for (int i = 0; i < blockPoses.size(); i++) {
            nbt.put("pos" + i, putBlockPos(blockPoses.get(i)));
        }

        return nbt;
    }

    public static List<BlockPos> getListOfBlockPos(CompoundTag nbt) {
        int size = nbt.getInt("poses_size");
        List<BlockPos> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i, getBlockPos(nbt.getCompound("pos"+i)));
        }

        return list;
    }
}
