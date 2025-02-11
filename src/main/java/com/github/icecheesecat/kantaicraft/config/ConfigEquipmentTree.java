package com.github.icecheesecat.kantaicraft.config;

import com.github.icecheesecat.kantaicraft.equipment.EquipmentProperties;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigEquipmentTree {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    private static final Map<Integer, ForgeConfigSpec.ConfigValue<List<? extends Integer>>> EQUIPMENT_TREE_CONFIG = new HashMap<>();

    static {

        EQUIPMENT_TREE_CONFIG.put(Equipments.EMPTY.getId(),
                BUILDER.comment("Default destroyer equipment options")
                        .defineList("default_destroyer_equipment_options",
                                ImmutableList.of(101),
                                EquipmentProperties.ALL_PROPERTIES::containsKey));
        EQUIPMENT_TREE_CONFIG.put(101,
                BUILDER.comment("12 cm single gun mount")
                        .defineList("12cm_single_gun_mount",
                                ImmutableList.of(
                                        102, 103),
                                EquipmentProperties.ALL_PROPERTIES::containsKey));

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
