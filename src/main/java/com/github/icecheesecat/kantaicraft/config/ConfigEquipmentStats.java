package com.github.icecheesecat.kantaicraft.config;

import com.github.icecheesecat.kantaicraft.equipment.DefaultValue;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentProperties;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class ConfigEquipmentStats {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    private static final Map<Integer, EquipmentStats> ALL_EQUIPMENT_STATS = new HashMap<>();

    static {

        // TODO data generation from json file
        ALL_EQUIPMENT_STATS.put(EquipmentProperties.EMPTY.getId(),
                EquipmentStats.create(BUILDER, EquipmentProperties.EMPTY.getName()));
        Equipments.ALL_EQUIPMENTS.forEach((id, equipment) -> {
            var equipmentStats = EquipmentStats.create(BUILDER, equipment.getName().getString());
            DefaultValue defaultValue = equipment.defaultValue();
            defaultValue.getStats().forEach((equipmentStats::add));

            ALL_EQUIPMENT_STATS.put(id, equipmentStats);
        });

        SPEC = BUILDER.build();
    }

    public static double getEquipmentStatsById(int id, EquipmentStatType type) {
        EquipmentStats stats = ALL_EQUIPMENT_STATS.get(id);

        return stats.getValue(type);
    }

    public static Map<EquipmentStatType, Double> createMap(int id) {
        var stats = ALL_EQUIPMENT_STATS.get(id);
        Map<EquipmentStatType, Double> nMap = new HashMap<>();
        for (var ele: stats.getStats().entrySet()) {
            nMap.put(ele.getKey(), ele.getValue().get()) ;
        }

        return nMap;
    }


}
