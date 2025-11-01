package com.github.icecheesecat.kantaicraft.equipment.handler;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class ArmedEquipment implements INBTSerializable<CompoundTag> {

    Equipment equipment;
    String equippedOnName;

    public ArmedEquipment() {

    }

    public ArmedEquipment(Equipment equipment, String equippedOnName) {
        this.equipment = equipment;
        this.equippedOnName = equippedOnName;
    }

    public static ArmedEquipment empty() {
        return new ArmedEquipment(Equipment.EMPTY, "");
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("equipment", this.equipment.serializeNBT());
        nbt.putString("equippedOnName", this.equippedOnName);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.equipment = new Equipment();
        this.equipment.deserializeNBT(nbt.getCompound("equipment"));
        this.equippedOnName = nbt.getString("equippedOnName");
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public String getEquippedOnName() {
        return equippedOnName;
    }
}
