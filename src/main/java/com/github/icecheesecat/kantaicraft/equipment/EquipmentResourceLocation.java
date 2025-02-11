package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class EquipmentResourceLocation {
    public static final Map<Integer, ResourceLocation> ALL_EQUIPMENTS_RESOURCE_LOCATION = new HashMap<>();

    static {
        ALL_EQUIPMENTS_RESOURCE_LOCATION.put(101, new ResourceLocation(KantaiCraft.MODID, "textures/equipment/12cm_small_gun_mount.png"));
    }

    public static ResourceLocation getResourceById(int uid) {
        return ALL_EQUIPMENTS_RESOURCE_LOCATION.getOrDefault(uid, null);
    }
}
