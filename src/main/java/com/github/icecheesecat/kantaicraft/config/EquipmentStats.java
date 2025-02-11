package com.github.icecheesecat.kantaicraft.config;

import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class EquipmentStats {
    public Map<EquipmentStatType, ForgeConfigSpec.DoubleValue> configStats = new HashMap<>();

    public double get(EquipmentStatType statType) {
        return configStats.get(statType).get();
    }

    public void add(EquipmentStatType type, ForgeConfigSpec.DoubleValue doubleValue) {
        configStats.put(type, doubleValue);
    }

}
