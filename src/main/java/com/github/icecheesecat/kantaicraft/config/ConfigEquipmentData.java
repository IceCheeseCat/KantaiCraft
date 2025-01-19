package com.github.icecheesecat.kantaicraft.config;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigEquipmentData {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    private static final Map<Integer, ForgeConfigSpec.ConfigValue<List<? extends Integer>>> EQUIPMENT_TREE_CONFIG = new HashMap<>();
//    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> EMPTY_DESTROYER;

    static {
//        EMPTY_DESTROYER = BUILDER.comment("Default destroyer equipment options test")
//                        .defineList("default_destroyer_equipment_options_test",
//                                ImmutableList.of(Equipments.__12cm_single_gun_mount__.getUid()),
//                                Equipments.ALL_EQUIPMENTS_MAP::containsKey);

        EQUIPMENT_TREE_CONFIG.put(Equipment.EMPTY.getId(),
                BUILDER.comment("Default destroyer equipment options")
                        .defineList("default_destroyer_equipment_options",
                                ImmutableList.of(Equipments.__12cm_single_gun_mount__.getId()),
                                Equipments.ALL_EQUIPMENTS_MAP::containsKey));
        EQUIPMENT_TREE_CONFIG.put(Equipments.__12cm_single_gun_mount__.getId(),
                BUILDER.comment("12 cm single gun mount")
                        .defineList("12cm_single_gun_mount",
                                ImmutableList.of(
                                        Equipments.__12cm_twin_gun_mount__.getId(),
                                        Equipments.__12cm_twin_gun_mount_model_B_kai_2__.getId()),
                                Equipments.ALL_EQUIPMENTS_MAP::containsKey));

        SPEC = BUILDER.build();
    }

    public static List<? extends Integer> getEquipmentById(int uid) {
        if (EQUIPMENT_TREE_CONFIG.containsKey(uid)) {
            return EQUIPMENT_TREE_CONFIG.get(uid).get();
        }
        else {
            System.err.println("Config Equipment Map does not contain key = " + uid);
            return ImmutableList.of(-1);
        }
    }

}
