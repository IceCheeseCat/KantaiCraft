package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class Equipment {

    protected Map<EquipmentStatType, Double> stats = new HashMap<>();
    private EquipmentLevel equipmentLevel;
    private int uid;
    private String name;

    protected Equipment(Equipment equipment) {
        this.stats.putAll(equipment.stats);
        this.uid = equipment.getUid();
        this.name = equipment.getName();
    }

    public Equipment(int uid, String name, EquipmentLevel level) {
        this.uid = uid;
        this.equipmentLevel = level;
        this.name = name;
    }

    public Equipment(int uid, String name, EquipmentLevel level, Map<EquipmentStatType, Double> stats) {
        this.uid = uid;
        this.equipmentLevel = level;
        this.name = name;
        this.stats.putAll(stats);
    }

    public Equipment addStat(EquipmentStatType type, Double v) {
        this.stats.put(type, v);
        return this;
    }

    public double getStat(EquipmentStatType type) {
        return stats.get(type);
    }

    private void setStats(Map<EquipmentStatType, Double> s) {
        this.stats = s;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public EquipmentLevel getEquipmentLevel() {
        return equipmentLevel;
    }

    public void setEquipmentLevel(@NotNull EquipmentLevel level) {
        this.equipmentLevel = level;
    }

    public EquipmentType getType() {
        switch (uid/100) {
            case 2 -> {
                return EquipmentType.PLANE;
            }
            case 1 -> {
                return EquipmentType.CANNON;
            }
            default -> {
                return EquipmentType.NONE;
            }
        }
    }

    public Equipment asCopy() {
        return new Equipment(this) {};
    }

    public String debugString() {
        return "[Uid = " + this.uid + "]\n" + equipmentLevel.debugString();
    }

}
