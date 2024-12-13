package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.equipment.cannon.Cannon;

import java.util.HashMap;
import java.util.Map;

public class Equipments {

    public static final Equipment __12cmSingleGunMount__;

    private static final Map<Integer, Equipment> EQUIPMENTS;

    private static Equipment get(int uid) {
        if (!EQUIPMENTS.containsKey(uid)) {
            throw new IllegalArgumentException("No such uid for equipment.");
        }
        else {
            return EQUIPMENTS.get(uid);
        }
    }

    public static Equipment getInstance(int uid, EquipmentLevel level) {
        return get(uid).asCopy(level);
    }

    static {
        __12cmSingleGunMount__ = new Cannon(101, "12 cm Single Gun Mount", 100, 2.0f, 30.0f, 0);
        __12cmSingleGunMount__.addStat(EquipmentStatType.FIREPOWER, 5.0d);

        EQUIPMENTS = new HashMap<>();
        EQUIPMENTS.put(101, __12cmSingleGunMount__);
    }
}
