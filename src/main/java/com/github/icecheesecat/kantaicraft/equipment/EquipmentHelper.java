package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class EquipmentHelper {

    public static final Equipment TwelveCMSingleGunMount = new Equipment(EquipmentType.CANNON, 101, EquipmentLevel.NULL);
    public static final Map<Integer, ResourceLocation> EQUIPMENTS = Map.ofEntries(
            Map.entry(TwelveCMSingleGunMount.getUid(), new ResourceLocation(KantaiCraft.MODID, "textures/equipment/12cm_small_gun_mount.png"))
    );

}
