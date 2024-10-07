package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class Equipment implements INBTSerializable<CompoundTag> {
    private EquipmentLevel equipmentLevel;
    private int uid;
    protected String name;

    public Equipment(EquipmentLevel level, int uid) {
        this.uid = uid;
        this.equipmentLevel = level;
    }


    public int getUid() {
        return uid;
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
            case 1 -> {
                return EquipmentType.CANNON;
            }
            default -> {
                return EquipmentType.NONE;
            }
        }
    }

    public static final Equipment EMPTY = new Equipment(EquipmentLevel.NULL,-1);
}
