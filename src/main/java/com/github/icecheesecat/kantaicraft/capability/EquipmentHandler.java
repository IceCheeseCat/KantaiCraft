package com.github.icecheesecat.kantaicraft.capability;

import com.github.icecheesecat.kantaicraft.entity.ship.ISlotCheckerEntity;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
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
        this.equipments = NonNullList.withSize(size, Equipment.EMPTY);
        this.dirty = NonNullList.withSize(size, false);
    }

    public boolean canApplyAtSlot(int i, Equipment equipment, ISlotCheckerEntity slotCheckerEntity) {
        return slotCheckerEntity.get(i).contains(equipment.getType());
    }

    public void setEquipment(int i, Equipment equipment, ISlotCheckerEntity slotCheckerEntity) {
        if (canApplyAtSlot(i, equipment, slotCheckerEntity)) {
            Equipment r = this.equipments.set(i, equipment);
            this.dirty.set(i, true);
        }
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

            nbt.put(str, equipment.save());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.slotSize = nbt.getInt("equipmenthandler.size");
        for (int i = 0; i < this.slotSize; i++) {
            String str = "equipmenthandler.equipment." + i;
            Equipment equipment = Equipment.EMPTY.asCopy();

            this.equipments.set(i, equipment.load((CompoundTag) nbt.get(str)));
        }
    }

    public int getSlotSize() {
        return slotSize;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

}
