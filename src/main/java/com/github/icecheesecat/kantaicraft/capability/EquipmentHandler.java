package com.github.icecheesecat.kantaicraft.capability;

import com.github.icecheesecat.kantaicraft.entity.ship.ISlotCheckerEntity;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Set;

public class EquipmentHandler implements INBTSerializable<CompoundTag> {
    public static final ImmutableSet<EquipmentType> CANNON_WEAPON = ImmutableSet.of(EquipmentType.SMALL_CANNON, EquipmentType.MEDIUM_CANNON, EquipmentType.LARGE_CANNON);
    public static final ImmutableSet<EquipmentType> ATTACK_AIRCRAFT = ImmutableSet.of(EquipmentType.AIRCRAFT_DIVE_BOMBER, EquipmentType.AIRCRAFT_TORPEDO_BOMBER);
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

    public boolean hasAnyOfType(EquipmentType type) {
        return this.equipments.stream().anyMatch(equipment -> equipment.isTypeOf(type));
    }

    public boolean hasRangeAttackWeapon() {
        return this.equipments.stream().anyMatch(equipment -> CANNON_WEAPON.stream().anyMatch(equipment::isTypeOf)) ||
        this.equipments.stream().anyMatch(equipment -> ATTACK_AIRCRAFT.stream().anyMatch(equipment::isTypeOf));
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
