package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.INBTSerializable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class EquipmentSlots implements INBTSerializable<CompoundTag> {

    private List<Equipment> equipments;
    private List<SlotChecker> eachSlotChecker;
    private int slotSize;

    public EquipmentSlots(int size, SlotChecker slotChecker) {
        this.slotSize = size;
        this.equipments = new ArrayList<>();
        this.eachSlotChecker = new ArrayList<>();
        for (int i = 0; i < slotSize; i++) {
            this.equipments.add(Equipment.EMPTY);
            this.eachSlotChecker.add(slotChecker);
        }
    }

    public boolean canApplyAtSlot(int i, Equipment equipment) {
        return this.eachSlotChecker.get(i).contains(equipment.getType());
    }

    public ResourceRefund applyAndRefund(int i, Equipment equipment) {
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

    public List<Equipment> getEquipments() {
        return equipments;
    }

//    public ByteBuffer writeBuffer() {
//
//        ByteBuffer buffer = ByteBuffer.allocate(slotSize * Integer.BYTES * 5);
//        for (int i = 0; i < slotSize; i++) {
//            Equipment equipment = equipments.get(i);
//            buffer.putInt(equipment.getEquipmentType().ordinal());
//            buffer.putInt(equipment.getUid());
//            buffer.putInt(equipment.getEquipmentLevel().getLevel());
//            buffer.putFloat(equipment.getEquipmentLevel().getDifficulty());
//        }
//
//        for (int i = 0; i < slotSize; i++) {
//            buffer.putInt(equipmentTypes.get(i).ordinal());
//        }
//
//        return buffer;
//    }
//
//    public static EquipmentSlots readBuffer(int size, ByteBuffer buffer) {
//
//        int slotSize = size;
//        buffer.rewind();
//
//        List<Equipment> equipments = new ArrayList<>();
//        for (int i = 0; i < slotSize; i++) {
//            equipments.add(new Equipment(EquipmentType.get(buffer.getInt()), buffer.getInt(), new EquipmentLevel(buffer.getInt(), buffer.getFloat())));
//        }
//        List<EquipmentType> equipmentTypes = new ArrayList<>();
//        for (int i = 0; i < slotSize; i++) {
//            equipmentTypes.add(EquipmentType.get(buffer.getInt()));
//        }
//
//        return new EquipmentSlots(slotSize, equipments, equipmentTypes);
//    }


}
