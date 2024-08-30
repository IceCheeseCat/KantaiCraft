package com.github.icecheesecat.shincolle.util;

import com.github.icecheesecat.shincolle.equipment.EquipmentLevel;
import com.github.icecheesecat.shincolle.equipment.ResourceRefund;
import com.github.icecheesecat.shincolle.equipment.Equipment;
import com.github.icecheesecat.shincolle.equipment.EquipmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.INBTSerializable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class EquipmentSlots implements INBTSerializable<CompoundTag> {

    public List<Equipment> equipments;
    public List<EquipmentType> equipmentTypes;
    private int slotSize = 4;

    public EquipmentSlots(int size, List<EquipmentType> equipmentTypes) {
        this.slotSize = size;
        this.equipments = new ArrayList<>();
        for (int i = 0; i < slotSize; i++) {
            this.equipments.add(Equipment.EMPTY);
        }

        if (equipmentTypes.size() < this.slotSize) {
            throw new RuntimeException("Weapon slots' equipment types size must match parameter size.");
        }
        this.equipmentTypes = equipmentTypes;
    }

    public EquipmentSlots(List<EquipmentType> equipmentTypes) {
        this.equipments = new ArrayList<>();
        for (int i = 0; i < slotSize; i++) {
            this.equipments.add(Equipment.EMPTY);
        }

        if (equipmentTypes.size() < this.slotSize) {
            throw new RuntimeException("Weapon slots' equipment types size must match parameter size.");
        }
        this.equipmentTypes = equipmentTypes;
    }

    public EquipmentSlots(int slotSize, List<Equipment> equipments, List<EquipmentType> equipmentTypes) {
        this.slotSize = slotSize;
        this.equipments = equipments;

        if (equipmentTypes.size() < this.slotSize) {
            throw new RuntimeException("Weapon slots' equipment types size must match parameter size.");
        }
        this.equipmentTypes = equipmentTypes;
    }

    public Equipment getSlot(int i) {
        return this.equipments.get(i);
    }

    public ResourceRefund applyAtSlot(int i, Equipment equipment) {
        Equipment r = this.equipments.set(i, equipment);

        return ResourceRefund.get(r.getUid());
    }

    public ResourceRefund removeFromSlot(int i) {

        Equipment r = this.equipments.set(i, Equipment.EMPTY);
        return ResourceRefund.get(r.getUid());

    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("equipmentslots.size", slotSize);
        for (int i = 0; i < equipments.size(); i++) {
            String str = "equipmentslots." + i;
            nbt.put(str, equipments.get(i).serializeNBT());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.slotSize = nbt.getInt("equipmentslots.size");
        for (int i = 0; i < this.slotSize; i++) {
            String str = "equipmentslots." + i;

            Equipment equipment = Equipment.EMPTY;
            equipment.deserializeNBT(nbt);
            this.equipments.set(i, equipment);
        }
    }

    public int getSlotSize() {
        return slotSize;
    }

    public void writeBuffer(FriendlyByteBuf buf) {

        buf.writeInt(slotSize);

        ByteBuffer buffer = ByteBuffer.allocate(slotSize * Integer.BYTES * 5);
        for (int i = 0; i < slotSize; i++) {
            Equipment equipment = equipments.get(i);
            buffer.putInt(equipment.getWeaponType().ordinal());
            buffer.putInt(equipment.getUid());
            buffer.putInt(equipment.getWeaponLevel().getLevel());
            buffer.putFloat(equipment.getWeaponLevel().getDifficulty());
        }

        for (int i = 0; i < slotSize; i++) {
            buffer.putInt(equipmentTypes.get(i).ordinal());
        }

        buffer.rewind();
        buf.writeByteArray(buffer.array());

    }

    public static EquipmentSlots readBuffer(FriendlyByteBuf buf) {

        int slotSize = buf.readInt();

        ByteBuffer buffer = ByteBuffer.wrap(buf.readByteArray());
        buffer.rewind();

        List<Equipment> equipments = new ArrayList<>();
        for (int i = 0; i < slotSize; i++) {
            equipments.add(new Equipment(EquipmentType.get(buffer.getInt()), buffer.getInt(), new EquipmentLevel(buffer.getInt(), buffer.getFloat())));
        }
        List<EquipmentType> equipmentTypes = new ArrayList<>();
        for (int i = 0; i < slotSize; i++) {
            equipmentTypes.add(EquipmentType.get(buffer.getInt()));
        }

        return new EquipmentSlots(slotSize, equipments, equipmentTypes);
    }


}
