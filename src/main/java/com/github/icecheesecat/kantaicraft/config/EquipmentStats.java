package com.github.icecheesecat.kantaicraft.config;

import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.invoke.WrongMethodTypeException;
import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.Map;

public class EquipmentStats {
    private Map<EquipmentStatType, ForgeConfigSpec.DoubleValue> configStats = new HashMap<>();

    String path;
    ForgeConfigSpec.Builder BUILDER;

    private EquipmentStats(ForgeConfigSpec.Builder BUILDER, String path) {
        this.path = path;
        this.BUILDER = BUILDER;
    }

    public static EquipmentStats create(ForgeConfigSpec.Builder BUILDER, String path) {
        return new EquipmentStats(BUILDER, path);
    }

    private String createTitleForStat(EquipmentStatType equipmentStatType) {
        return equipmentStatType.name();
    }

    private String createPathNameForStat(EquipmentStatType equipmentStatType) {
        return this.path + "." + equipmentStatType.name();
    }

    public EquipmentStats add(EquipmentStatType type, double val) {
        ForgeConfigSpec.DoubleValue doubleValue =
                BUILDER.comment(createTitleForStat(type))
                        .defineInRange(createPathNameForStat(type), () -> val, type.getMin(), type.getMax());
        configStats.put(type, doubleValue);

        return this;
    }

    public double getValue(EquipmentStatType statType) {
        if (!configStats.containsKey(statType)) {
            throw new IllegalStateException("No such datatype in this equipment: " + path + " -> " + statType.name());
        }

        return configStats.get(statType).get();
    }

//    public int getInt(EquipmentStatType statType) {
//        if (!statType.isInt()) {
//            throw new WrongMethodTypeException("Your datatype is " + statType.name());
//        }
//        if (!configStats.containsKey(statType)) {
//            throw new IllegalStateException("No such datatype in this equipment: " + path + " -> " + statType.name());
//        }
//
//        return (int) (double) configStats.get(statType).get();
//    }
//
//    public long getLong(EquipmentStatType statType) {
//        if (!statType.isLong()) {
//            throw new WrongMethodTypeException("Your datatype is " + statType.name());
//        }
//        if (!configStats.containsKey(statType)) {
//            throw new IllegalStateException("No such datatype in this equipment: " + path + " -> " + statType.name());
//        }
//
//        return (long) (double) configStats.get(statType).get();
//    }

    public Map<EquipmentStatType, ForgeConfigSpec.DoubleValue> getStats() {
        return configStats;
    }

}
