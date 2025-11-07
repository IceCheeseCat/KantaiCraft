package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class EquipmentType {
    private final EquipmentProperties equipmentProperties;
    private final Map<EquipmentStatType, Double> defaultStats;

    public EquipmentType(EquipmentProperties equipmentProperties) {
        this.equipmentProperties = equipmentProperties;
        this.defaultStats = new HashMap<>();
    }

    public void setDefaultStats(Map<EquipmentStatType, Double> stats) {
        this.defaultStats.clear();
        this.defaultStats.putAll(stats);
    }

    public Equipment create() {
        return new Equipment(this);
    }

    public Equipment create(int level) {

        return new Equipment(this);
    }

    public EquipmentProperties getEquipmentProperties() {
        return equipmentProperties;
    }

    public int getId() {
        return equipmentProperties.getId();
    }

    public Map<EquipmentStatType, Double> getDefaultStats() {
        return defaultStats;
    }

    public Component getName() {
        return this.equipmentProperties.getName();
    }

    @Override
    public String toString() {
        return "[id=" + this.equipmentProperties.getId() + ", name=" + this.equipmentProperties.getString() +"]";
    }
}
