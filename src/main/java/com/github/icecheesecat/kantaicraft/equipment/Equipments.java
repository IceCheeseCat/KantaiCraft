package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.equipment.cannon.Cannon;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Map;

public class Equipments {

    public static final Equipment EMPTY_DESTROYER = new Equipment(-1, "null", EquipmentLevel.NULL){};
    public static final Equipment EMPTY_CARRIER = new Equipment(-2, "null", EquipmentLevel.NULL){};
    public static final Equipment __12cm_single_gun_mount__;
    public static final Equipment __12cm_twin_gun_mount__;
    public static final Equipment __12cm_twin_gun_mount_model_B_kai_2__;
    public static final Map<Integer, Equipment> ALL_EQUIPMENTS_MAP = new HashMap<>();

    static {
        __12cm_single_gun_mount__ = new Cannon(101, "12 cm Single Gun Mount", 100, 2.0f, 30.0f, 0)
                .addStat(EquipmentStatType.FIREPOWER, 5.0d);
        __12cm_twin_gun_mount__ = new Cannon(102, "12 cm Twin Gun Mount", 100, 2.0f, 30.0f, 0)
                .addStat(EquipmentStatType.FIREPOWER, 10.0d);
        __12cm_twin_gun_mount_model_B_kai_2__ = new Cannon(103, "12 cm Twin Gun Mount Model B Kai 2", 100, 2.0f, 30.0f, 0)
                .addStat(EquipmentStatType.FIREPOWER, 15.0d);

        ALL_EQUIPMENTS_MAP.put(EMPTY_DESTROYER.getUid(), EMPTY_DESTROYER);
        ALL_EQUIPMENTS_MAP.put(EMPTY_CARRIER.getUid(), EMPTY_CARRIER);
        ALL_EQUIPMENTS_MAP.put(__12cm_single_gun_mount__.getUid(), __12cm_single_gun_mount__);
        ALL_EQUIPMENTS_MAP.put(__12cm_twin_gun_mount__.getUid(), __12cm_twin_gun_mount__);
        ALL_EQUIPMENTS_MAP.put(__12cm_twin_gun_mount_model_B_kai_2__.getUid(), __12cm_twin_gun_mount_model_B_kai_2__);
    }

    public static Equipment getEquipmentInstanceById(int uid) {
        Equipment equipment = ALL_EQUIPMENTS_MAP.getOrDefault(uid, null);
        if (equipment != null) {
            return equipment.asCopy();
        }
        else {
            System.err.println("Equipments does not contain key = " + uid);
            return null;
        }
    }

}
