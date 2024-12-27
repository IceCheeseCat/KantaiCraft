package com.github.icecheesecat.kantaicraft.equipment.plane;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentLevel;

public abstract class Aircraft extends Equipment {

    public Aircraft(int uid, String name, EquipmentLevel level) {
        super(uid, name, level);
    }
}
