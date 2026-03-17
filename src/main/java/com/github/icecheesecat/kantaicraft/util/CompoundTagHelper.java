package com.github.icecheesecat.kantaicraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class CompoundTagHelper {

    public static CompoundTag getNbtFromVec3(Vec3 vec3) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("x", vec3.x);
        nbt.putDouble("y", vec3.y);
        nbt.putDouble("z", vec3.z);

        return nbt;
    }
    
    public static Vec3 getVec3(CompoundTag nbt) {
        double x = 0, y = 0, z = 0;
        if (nbt.contains("x")) {
            x = nbt.getDouble("x");
        }
        if (nbt.contains("y")) {
            y = nbt.getDouble("y");
        }
        if (nbt.contains("z")) {
            z = nbt.getDouble("z");
        }
        
        return new Vec3(x, y, z);
    }

    public static CompoundTag writeBlockPos(BlockPos blockPos) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("pos_x", blockPos.getX());
        nbt.putInt("pos_y", blockPos.getY());
        nbt.putInt("pos_z", blockPos.getZ());
        return nbt;
    }

    public static BlockPos readBlockPos(CompoundTag nbt) {
        return new BlockPos(nbt.getInt("pos_x"), nbt.getInt("pos_y"), nbt.getInt("pos_z"));
    }

    public static CompoundTag getNbtFromListOfBlockPos(List<BlockPos> blockPoses) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("poses_size", blockPoses.size());
        for (int i = 0; i < blockPoses.size(); i++) {
            nbt.put("pos" + i, writeBlockPos(blockPoses.get(i)));
        }

        return nbt;
    }

    public static List<BlockPos> getListOfBlockPos(CompoundTag nbt) {
        int size = nbt.getInt("poses_size");
        List<BlockPos> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i, readBlockPos(nbt.getCompound("pos"+i)));
        }

        return list;
    }

    public static <T> void serializeList(CompoundTag nbt, String tagName, @NotNull List<T> objects, Function<T, CompoundTag> objectWriter) {

        CompoundTag nbt1 = new CompoundTag();
        nbt1.putInt("size", objects.size());

        for (int i = 0; i < objects.size(); i++) {
            CompoundTag eTag = objectWriter.apply(objects.get(i));
            nbt1.put("e" + i, eTag);
        }

        nbt.put(tagName, nbt1);
    }

    @NotNull
    public static <T> List<T> deserializeList(CompoundTag nbt, String tagName, Function<CompoundTag, T> objectReader) {
        if (!nbt.contains(tagName)) return new ArrayList<>();

        CompoundTag nbt1 = nbt.getCompound(tagName);
        if (!nbt1.contains("size")) return new ArrayList<>();

        List<T> returnList = new ArrayList<>();
        for (int i = 0; i < nbt1.getInt("size"); i++) {
            if (!nbt1.contains("e" + i)) return new ArrayList<>();
            returnList.add(i, objectReader.apply(nbt1.getCompound("e" + i)));
        }

        return returnList;
    }
}
