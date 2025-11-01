package com.github.icecheesecat.kantaicraft.equipment.handler;

import com.github.icecheesecat.kantaicraft.entityship.entity.ISlotCheckerEntity;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.List;
import java.util.stream.Collectors;

public class EquipmentHandler implements INBTSerializable<CompoundTag> {
    public static final ImmutableSet<EquipmentType> CANNON_WEAPON = ImmutableSet.of(EquipmentType.SMALL_CANNON, EquipmentType.MEDIUM_CANNON, EquipmentType.LARGE_CANNON);
    public static final ImmutableSet<EquipmentType> ATTACK_AIRCRAFT = ImmutableSet.of(EquipmentType.AIRCRAFT_DIVE_BOMBER, EquipmentType.AIRCRAFT_TORPEDO_BOMBER);
    private NonNullList<ArmedEquipment> equipments;
    private NonNullList<Boolean> dirty;
    private int slotSize;



    public EquipmentHandler(int size) {
        this.slotSize = size;
        this.equipments = NonNullList.withSize(size, ArmedEquipment.empty());
        this.dirty = NonNullList.withSize(size, true);
    }

    protected EquipResult tryApplyAtSlot(int i, String equippedOnName, Equipment equipment, ISlotCheckerEntity slotCheckerEntity) {
        if (i >= slotSize || i < 0) {
            return EquipResult.OUT_OF_INDEX;
        }
        else if (! slotCheckerEntity.getSlotChecker(i).contains(equipment.getType())) {
            return EquipResult.CANNOT_EQUIP_THIS_TYPE;
        }
        else if (hasAlreadyEquippedSameBodyPart(equippedOnName)) {
            return EquipResult.BODY_PART_HAS_USED;
        }

        return EquipResult.SUCCESS;
    }

    public boolean hasAlreadyEquippedSameBodyPart(String equippedOnName) {
        return this.equipments.stream().anyMatch(armedEquipment -> armedEquipment.equippedOnName.equals(equippedOnName));
    }

    /**
     *
     * @param i
     * @param equippedOnName
     * @param equipment
     * @param slotCheckerEntity
     * @return Equipment -> returns back the equipment that was equipped before set.
     */
    public Equipment setEquipment(int i, String equippedOnName, Equipment equipment, ISlotCheckerEntity slotCheckerEntity) {

        return switch (tryApplyAtSlot(i, equippedOnName, equipment, slotCheckerEntity)) {
            case SUCCESS -> {
                Equipment r = this.equipments.set(i, new ArmedEquipment(equipment, equippedOnName)).getEquipment();
                this.dirty.set(i, true);

                yield r;
            }
            default -> equipment;
        };
    }

    public Equipment setEquipment(int i, ArmedEquipment armedEquipment, ISlotCheckerEntity slotCheckerEntity) {
        return this.setEquipment(i, armedEquipment.equippedOnName, armedEquipment.getEquipment(), slotCheckerEntity);
    }

    public void setOnClient(int i, ArmedEquipment equipment) {
        this.equipments.set(i, equipment);
        this.dirty.set(i, true);
    }


    public boolean isDirty(int index) {
        return this.dirty.get(index);
    }

    public void setNotDirty(int index) {
        this.dirty.set(index, false);
    }

    public Equipment getEquipment(int index) {
        return this.equipments.get(index).getEquipment();
    }

    public ArmedEquipment getArmedEquipment(int index) {
        return this.equipments.get(index);
    }

    public NonNullList<ArmedEquipment> getArmedEquipments() {
        return this.equipments;
    }

    public boolean sameAsBodyPartName(int index, GeoBone bone) {
        return this.equipments.get(index).equippedOnName.equals(bone.getName());
    }

    public boolean hasRangeAttackWeapon() {
        return this.equipments.stream().anyMatch(equipment -> CANNON_WEAPON.stream().anyMatch(equipment.getEquipment()::isTypeOf)) ||
        this.equipments.stream().anyMatch(equipment -> ATTACK_AIRCRAFT.stream().anyMatch(equipment.getEquipment()::isTypeOf));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("equipmenthandler.size", slotSize);
        for (int i = 0; i < equipments.size(); i++) {
            nbt.put("equipmenthandler.armedEquipment." + i, this.equipments.get(i).serializeNBT());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.slotSize = nbt.getInt("equipmenthandler.size");
        for (int i = 0; i < this.slotSize; i++) {
            ArmedEquipment equipment = new ArmedEquipment();
            equipment.deserializeNBT(nbt.getCompound("equipmenthandler.armedEquipment." + i));
            this.equipments.set(i, equipment);
        }
    }

    public int getSlotSize() {
        return slotSize;
    }

    public List<Equipment> getEquipments() {
        return equipments.stream().map(ArmedEquipment::getEquipment).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < this.slotSize; i++) {
            string += i + "[ equipment: " + this.equipments.get(i).getEquipment() + " bodyPart = " + this.equipments.get(i).equippedOnName + "]\n";
        }
        string += "\n";
        return string;
    }
}
