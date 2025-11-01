package com.github.icecheesecat.kantaicraft.equipment.handler;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.model.equipment.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class ArmedEquipment implements INBTSerializable<CompoundTag> {

    Equipment equipment;
    BodyPart armedBodyPart;

    public ArmedEquipment() {

    }

    public ArmedEquipment(Equipment equipment, BodyPart armedBodyPart) {
        this.equipment = equipment;
        this.armedBodyPart = armedBodyPart;
    }

    public static ArmedEquipment empty() {
        return new ArmedEquipment(Equipment.EMPTY, BodyPart.none);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("equipment", this.equipment.serializeNBT());
        nbt.putInt("armedBodyPart", this.armedBodyPart.ordinal());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.equipment = new Equipment();
        this.equipment.deserializeNBT(nbt.getCompound("equipment"));
        this.armedBodyPart = BodyPart.values()[nbt.getInt("armedBodyPart")];
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public BodyPart getArmedBodyPart() {
        return armedBodyPart;
    }
}
