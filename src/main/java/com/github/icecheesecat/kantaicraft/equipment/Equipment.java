package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Equipment {

    protected Map<EquipmentStatType, Double> stats = new HashMap<>();
    private int id;
    private Component name;
    private int level;
    private EquipmentType type;

    public static final int MAX_LEVEL = 10;
    protected List<EquipmentStatType> requiredStats = new ArrayList<>();

    public Equipment(Equipment equipment) {
        this(equipment.id, equipment.name, equipment.type, equipment.stats);
        this.requiredStats = List.copyOf(equipment.requiredStats);
    }

    public Equipment(int id, Component name, EquipmentType type, Map<EquipmentStatType, Double> stats) {
        this.id = id;
        this.name = name;
        this.level = 0;
        this.type = type;
        this.stats.putAll(stats);
        if (EquipmentType.calculateType(id) != this.getType()) {
            throw new IllegalStateException("Not valid id ("+ id + ") for " + this.getClass().getName() + " related to " + this.getType().name() + "\n.");
        }
        if (!this.checkStats()) {
            throw new IllegalStateException("Missing stats in " + this.getClass() + " (" + this.getName() + ").\n");
        }
    }

    public double getStat(EquipmentStatType type) {
        return stats.get(type);
    }

    public void setStats(Map<EquipmentStatType, Double> s) {
        this.stats = s;
    }

    public Map<EquipmentStatType, Double> getStats() {
        return this.stats;
    }

    public int getId() {
        return id;
    }

    public Component getName() {
        return this.name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void doLevelUp() {
        this.level++;
    }

    public EquipmentType getType() {
        return this.type;
    }

    public abstract Equipment asCopy();

//    public CompoundTag save() {
//        CompoundTag nbt = new CompoundTag();
//        nbt.putInt("equipment.id", this.id);
//        nbt.putInt("equipment.level", this.level);
//
//        return nbt;
//    }
//
//    public Equipment load(CompoundTag nbt) {
//        this.id = nbt.getInt("equipment.id");
//        this.level = nbt.getInt("equipment.level");
//
//        return Equipments.getEquipmentInstanceById(id, level);
//    }

    public boolean checkStats() {
        return this.requiredStats.stream().allMatch(this.stats::containsKey);
    }

    protected abstract void setRequiredStats();

}
