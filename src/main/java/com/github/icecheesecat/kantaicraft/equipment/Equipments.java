package com.github.icecheesecat.kantaicraft.equipment;

import java.util.HashMap;
import java.util.Map;

public class Equipments {

    public static final Equipment __12cm_single_gun_mount__;
    public static final Equipment __12cm_twin_gun_mount__;
    public static final Equipment __12cm_twin_gun_mount_model_B_kai_2__;
    public static final Map<Integer, Equipment> ALL_EQUIPMENTS_MAP = new HashMap<>();

    static {
        __12cm_single_gun_mount__ = new Equipment(101, "12 cm single gun mount")
                .addStat(EquipmentStatType.CANNON_SIZE, 0.0d)
                .addStat(EquipmentStatType.CANNON_RANGE, 30.0d)
                .addStat(EquipmentStatType.CANNON_COOLDOWN, 5.0d)
                .addStat(EquipmentStatType.CANNON_MISSLE_VELOCITY, 20.0d)
                .addStat(EquipmentStatType.FIREPOWER, 1.0d);

        __12cm_twin_gun_mount__ = new Equipment(102, "12 cm twin gun mount")
                .addStat(EquipmentStatType.CANNON_SIZE, 0.0d)
                .addStat(EquipmentStatType.CANNON_RANGE, 30.0d)
                .addStat(EquipmentStatType.CANNON_COOLDOWN, 5.0d)
                .addStat(EquipmentStatType.CANNON_MISSLE_VELOCITY, 20.0d)
                .addStat(EquipmentStatType.FIREPOWER, 2.0d);
        __12cm_twin_gun_mount_model_B_kai_2__ = new Equipment(103, "12 cm twin gun mount model B kai 2")
                .addStat(EquipmentStatType.CANNON_SIZE, 0.0d)
                .addStat(EquipmentStatType.CANNON_RANGE, 30.0d)
                .addStat(EquipmentStatType.CANNON_COOLDOWN, 5.0d)
                .addStat(EquipmentStatType.CANNON_MISSLE_VELOCITY, 20.0d)
                .addStat(EquipmentStatType.FIREPOWER, 2.0d)
                .addStat(EquipmentStatType.ANTIAIR, 1.0d);

        ALL_EQUIPMENTS_MAP.put(Equipment.EMPTY.getId(), Equipment.EMPTY);
        ALL_EQUIPMENTS_MAP.put(__12cm_single_gun_mount__.getId(), __12cm_single_gun_mount__);
        ALL_EQUIPMENTS_MAP.put(__12cm_twin_gun_mount__.getId(), __12cm_twin_gun_mount__);
        ALL_EQUIPMENTS_MAP.put(__12cm_twin_gun_mount_model_B_kai_2__.getId(), __12cm_twin_gun_mount_model_B_kai_2__);
    }

    public static Equipment getEquipmentInstanceById(int uid, int level) {
        Equipment equipment = ALL_EQUIPMENTS_MAP.getOrDefault(uid, null);
        if (equipment != null) {
            Equipment e = equipment.asCopy();
            e.setLevel(level);
            return e;
        }
        else {
            System.err.println("Equipments does not contain key = " + uid);
            return null;
        }
    }

}
