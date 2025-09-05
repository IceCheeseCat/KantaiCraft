package com.github.icecheesecat.kantaicraft.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public final class CompoundTagHelper {

    public static CompoundTag saveVec3(Vec3 vec3, String name) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble(name + ".x", vec3.x);
        nbt.putDouble(name + ".y", vec3.y);
        nbt.putDouble(name + ".z", vec3.z);

        return nbt;
    }
    
    public static Vec3 loadVec3(CompoundTag nbt, String name) {
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

}
