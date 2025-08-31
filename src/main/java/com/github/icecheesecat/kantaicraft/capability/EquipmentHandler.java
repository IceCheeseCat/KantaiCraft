package com.github.icecheesecat.kantaicraft.capability;

import com.github.icecheesecat.kantaicraft.entity.ship.ISlotCheckerEntity;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public class EquipmentHandler implements INBTSerializable<CompoundTag> {

    private NonNullList<Equipment> equipments;
    private NonNullList<Boolean> dirty;
    private int slotSize;

    public EquipmentHandler(int size) {
        this.slotSize = size;
        this.equipments = NonNullList.withSize(size, Equipments.EMPTY.asCopy());
        this.dirty = NonNullList.withSize(size, false);
    }

    protected boolean canApplyAtSlot(int i, Equipment equipment, ISlotCheckerEntity slotCheckerEntity) {
        return i < slotSize && slotCheckerEntity.getSlotChecker(i).contains(equipment.getType());
    }

    public Equipment setEquipment(int i, Equipment equipment, ISlotCheckerEntity slotCheckerEntity) {
        if (canApplyAtSlot(i, equipment, slotCheckerEntity)) {
            Equipment r = this.equipments.set(i, equipment);
            this.dirty.set(i, true);

            return r;
        }

        return equipment;
    }

    public void addEquipment(Equipment equipment, ISlotCheckerEntity slotCheckerEntity) {

    }

    public void setOnClient(int i, Equipment equipment) {
        this.equipments.set(i, equipment);
    }


    public boolean isDirty(int index) {
        return this.dirty.get(index);
    }

    public void setNotDirty(int index) {
        this.dirty.set(index, false);
    }

    public Equipment getEquipment(int index) {
        return this.equipments.get(index);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("equipmenthandler.size", slotSize);
        for (int i = 0; i < equipments.size(); i++) {
            String str = "equipmenthandler.equipment." + i;
            Equipment equipment = equipments.get(i);

            nbt.putInt(str + ".id", equipment.getId());
            nbt.putInt(str + ".level", equipment.getLevel());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.slotSize = nbt.getInt("equipmenthandler.size");
        for (int i = 0; i < this.slotSize; i++) {
            String str = "equipmenthandler.equipment." + i;
            int id = nbt.getInt(str + ".id");
            int level = nbt.getInt(str + ".level");

            Equipment equipment = Equipments.getEquipmentInstanceById(id, level);
            this.equipments.set(i, equipment);
        }
    }

    public int getSlotSize() {
        return slotSize;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < this.slotSize; i++) {
            string += i + ": " + this.equipments.get(i).getName() + "\n";
        }
        string += "\n";
        return string;
    }
}
