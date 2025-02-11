package com.github.icecheesecat.kantaicraft.config;

import com.github.icecheesecat.kantaicraft.equipment.EquipmentProperties;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigEquipmentStats {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    private static final Map<Integer, EquipmentStats> ALL_EQUIPMENT_STATS = new HashMap<>();

    static {

        ALL_EQUIPMENT_STATS.put(EquipmentProperties.EMPTY.getId(),
                new EquipmentStats());
        ALL_EQUIPMENT_STATS.put(EquipmentProperties.__12cm_single_gun_mount__.getId(),
                new CannonStats(BUILDER, EquipmentProperties.__12cm_single_gun_mount__.getName(), 0, 45.0d, 5.0d, 20.0d, 5.0d));
        ALL_EQUIPMENT_STATS.put(EquipmentProperties.__12cm_twin_gun_mount__.getId(),
                new CannonStats(BUILDER, EquipmentProperties.__12cm_twin_gun_mount__.getName(), 0, 45.0d, 5.0d, 20.0d, 6.0d));
        ALL_EQUIPMENT_STATS.put(EquipmentProperties.__12cm_twin_gun_mount_model_b_kai_2__.getId(),
                new CannonStats(BUILDER, EquipmentProperties.__12cm_twin_gun_mount_model_b_kai_2__.getName(), 0, 45.0d, 5.0d, 20.0d, 7.0d, 1.0d));

        SPEC = BUILDER.build();
    }

    public static double getEquipmentStatsById(int id, EquipmentStatType type) {
        EquipmentStats stats = ALL_EQUIPMENT_STATS.get(id);

        return stats.get(type);
    }

    public static Map<EquipmentStatType, Double> createMap(int id) {
        var stats = ALL_EQUIPMENT_STATS.get(id);
//        return stats.configStats.entrySet().stream().collect(
//                Collectors.toMap(entry -> entry.getKey(),
//                        (entry) -> entry.getValue().get(),
//                        null,
//                        HashMap::new));
        Map<EquipmentStatType, Double> nMap = new HashMap<>();
        for (var ele: stats.configStats.entrySet()) {
            nMap.put(ele.getKey(), ele.getValue().get()) ;
        }

        return nMap;
    }


}
