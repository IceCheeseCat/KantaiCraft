package com.github.icecheesecat.shincolle.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class Equipment implements INBTSerializable<CompoundTag> {
    private EquipmentType equipmentType;
    private EquipmentLevel equipmentLevel;
    private int uid;

    public Equipment(EquipmentType equipmentType, int uid, EquipmentLevel level) {
        this.equipmentType = equipmentType;
        this.uid = uid;
        this.equipmentLevel = level;
    }

    public EquipmentType getWeaponType() {
        return equipmentType;
    }

    public int getUid() {
        return uid;
    }

    public void setWeaponType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public boolean hasWeapon() {
        return this.equipmentType != EquipmentType.NULL;
    }

public EquipmentLevel getWeaponLevel() {
        return equipmentLevel;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt =  equipmentLevel.serializeNBT();
        nbt.putInt("equipment.uid", this.uid);
        nbt.putInt("equipment.equipmenttype", equipmentType.ordinal());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        equipmentLevel.deserializeNBT(nbt);
        this.equipmentType = EquipmentType.get(nbt.getInt("equipment.equipmenttype"));
        this.uid = nbt.getInt("equipment.uid");

    }

    public static final Equipment EMPTY = new Equipment(EquipmentType.NULL, -1, EquipmentLevel.NULL);
}
