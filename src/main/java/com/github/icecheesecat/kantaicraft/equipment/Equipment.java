package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public abstract class Equipment implements INBTSerializable<CompoundTag> {

    private Map<EquipmentStatType, Double> stats;
    private EquipmentLevel equipmentLevel;
    private int uid;
    private String name;

    public Equipment(int uid, String name, EquipmentLevel level) {
        this.uid = uid;
        this.equipmentLevel = level;
        this.name = name;
    }

    public void addStat(EquipmentStatType type, Double v) {
        this.stats.put(type, v);
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

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt =  equipmentLevel.serializeNBT();
        nbt.putInt("equipment.uid", this.uid);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        equipmentLevel.deserializeNBT(nbt);
        this.uid = nbt.getInt("equipment.uid");

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

    public Equipment asCopy(EquipmentLevel level) {
        Equipment n_equipment = new Equipment(this.uid, this.name, level){};
        return n_equipment;
    }

    public static final Equipment EMPTY = new Equipment(-1, "null", EquipmentLevel.NULL){};

}
