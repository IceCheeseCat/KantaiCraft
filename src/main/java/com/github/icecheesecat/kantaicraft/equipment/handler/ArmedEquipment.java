package com.github.icecheesecat.kantaicraft.equipment.handler;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class ArmedEquipment implements INBTSerializable<CompoundTag> {

    Equipment equipment;
    String equippableBoneName;

    public ArmedEquipment() {

    }

    public ArmedEquipment(Equipment equipment, String equippableBoneName) {
        this.equipment = equipment;
        this.equippableBoneName = equippableBoneName;
    }

    public static ArmedEquipment empty() {
        return new ArmedEquipment(EquipmentManager.createEmptyEquipment(), "");
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("equipment", this.equipment.serializeNBT());
        nbt.putString("equippedOnName", this.equippableBoneName);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.equipment = Equipment.makeFromCompoundTag(nbt.getCompound("equipment"));
        this.equippableBoneName = nbt.getString("equippedOnName");
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public String getEquippableBoneName() {
        return equippableBoneName;
    }
}
