package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;

public abstract class Equipment implements INBTSerializable<CompoundTag> {

    private ArrayList<EquipmentStat> stats;
    private EquipmentLevel equipmentLevel;
    private int uid;
    protected String name;

    public Equipment(EquipmentLevel level, int uid) {
        this.uid = uid;
        this.equipmentLevel = level;
        setupStats();
    }

    protected void addStat(EquipmentStat stat) {
        this.addStat(stat);
    }

    public int getUid() {
        return uid;
    }

    public EquipmentLevel getEquipmentLevel() {
        return equipmentLevel;
    }

    public abstract void setupStats();

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

    public static final Equipment EMPTY = new Equipment(EquipmentLevel.NULL,-1) {
        @Override
        public void setupStats() {
        }
    };

}
