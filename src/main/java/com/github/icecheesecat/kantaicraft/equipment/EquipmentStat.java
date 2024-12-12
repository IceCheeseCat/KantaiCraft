package com.github.icecheesecat.kantaicraft.equipment;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class EquipmentStat<U> {

    public EquipmentStatType type;
    public U value;

    public EquipmentStat(EquipmentStatType type, U value) {
        this.type = type;
        this.value = value;
    }

    public static <U> EquipmentStat<U> of(EquipmentStatType type, U value) {
        return new EquipmentStat<>(type, value);
    }

}


