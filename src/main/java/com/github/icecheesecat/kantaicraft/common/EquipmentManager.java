package com.github.icecheesecat.kantaicraft.common;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static com.github.icecheesecat.kantaicraft.common.Equipments.TwelveCMSmallGunMount;

public class EquipmentManager {

    public static final Map<Integer, ResourceLocation> EQUIPMENT_RESOURCES = Map.ofEntries(
            Map.entry(TwelveCMSmallGunMount.getUid(), new ResourceLocation(KantaiCraft.MODID, "textures/equipment/12cm_small_gun_mount.png"))
    );

    public static final Map<Integer, Equipment> EQUIPMENTS = Map.ofEntries(
            Map.entry(TwelveCMSmallGunMount.getUid(), TwelveCMSmallGunMount)
    );

    public static Equipment getEquipmentById(int uid) {
         Equipment e = EQUIPMENTS.get(uid);
        if (e == null) {
            return Equipment.EMPTY;
        }
        return e;
    }

}
