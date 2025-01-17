package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Set;

public class SlotChecker {

    Set<EquipmentType> set;

    public boolean contains(EquipmentType type) {
        return set.contains(type);
    }

    public static SlotChecker create(Set<EquipmentType> types) {
        SlotChecker sc = new SlotChecker();
        sc.set = types;
        return sc;
    }

}
