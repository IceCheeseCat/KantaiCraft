package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static com.github.icecheesecat.kantaicraft.equipment.Equipments.*;

public class EquipmentManager {

    private static final Map<Integer, ResourceLocation> EQUIPMENT_RESOURCES = Map.ofEntries(
            Map.entry(__12cmSingleGunMount__.getUid(), new ResourceLocation(KantaiCraft.MODID, "textures/equipment/12cm_small_gun_mount.png"))
    );

    public static ResourceLocation getResourceLocationByUid(int uid) {
        return EQUIPMENT_RESOURCES.get(uid);
    }

}
