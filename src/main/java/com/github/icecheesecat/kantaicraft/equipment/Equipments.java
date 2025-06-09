package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class Equipments {

    private static final Equipment EMPTY = new Equipment(EquipmentProperties.EMPTY);
    public static final Map<Integer, Equipment> ALL_EQUIPMENTS = new HashMap<>();

    static {
        createEquipment(EquipmentProperties.EMPTY);
        createEquipment(EquipmentProperties.__12cm_single_gun_mount__);
        createEquipment(EquipmentProperties.__12cm_twin_gun_mount__);
        createEquipment(EquipmentProperties.__12cm_twin_gun_mount_model_b_kai_2__);
        createEquipment(EquipmentProperties.__14cm_single_gun_mount__);
        createEquipment(EquipmentProperties.__155mm_single_gun_mount__);
        createEquipment(EquipmentProperties.__203mm_twin_gun_mount__);
        createEquipment(EquipmentProperties.__203mm_no3_single_gun_mount__);
        createEquipment(EquipmentProperties.__203mm_no4_single_gun_mount__);
        createEquipment(EquipmentProperties.__356mm_twin_gun_mount__);
        createEquipment(EquipmentProperties.__prototype_356mm_twin_gun_mount__);
        createEquipment(EquipmentProperties.__356mm_twin_gun_mount_kai_2__);
        createEquipment(EquipmentProperties.__41cm_twin_gun_mount__);
        createEquipment(EquipmentProperties.__prototype_41cm_twin_gun_mount__);
        createEquipment(EquipmentProperties.__41cm_twin_gun_mount_kai_2__);
        createEquipment(EquipmentProperties.__61cm_triple_torpedo_mount__);
        createEquipment(EquipmentProperties.__61cm_triple_oxygen_torpedo_mount__);
        createEquipment(EquipmentProperties.__61cm_quadruple_torpedo_mount__);
        createEquipment(EquipmentProperties.__61cm_quadruple_oxygen_torpedo_mount__);
        createEquipment(EquipmentProperties.__61cm_quintuple_oxygen_torpedo_mount__);
        createEquipment(EquipmentProperties.__type_13_air_radar__);
        createEquipment(EquipmentProperties.__type_13_air_radar_kai__);
        createEquipment(EquipmentProperties.__type_22_surface_radar__);
        createEquipment(EquipmentProperties.__type_22_surface_radar_kai_4__);
        createEquipment(EquipmentProperties.__type_33_surface_radar__);
        createEquipment(EquipmentProperties.__type_21_air_radar__);
        createEquipment(EquipmentProperties.__type_21_air_radar_kai__);
        createEquipment(EquipmentProperties.__type_32_surface_radar__);
        createEquipment(EquipmentProperties.__type_42_air_radar__);
        createEquipment(EquipmentProperties.__type_0_reconnaissance_seaplane__);
        createEquipment(EquipmentProperties.__type_0_reconnaissance_seaplane_model_11b__);
        createEquipment(EquipmentProperties.__type_0_reconnaissance_seaplane_model_11b_skilled__);
        createEquipment(EquipmentProperties.__type_0_observation_seaplane__);
        createEquipment(EquipmentProperties.__type_98_reconnaissance_seaplane_night_recon__);
        createEquipment(EquipmentProperties.__type_0_reconnaissance_seaplane_model_11b_kai_night_recon__);
        createEquipment(EquipmentProperties.__type_2_seaplane_fighter_kai__);
        createEquipment(EquipmentProperties.__kyoufuu_kai__);
        createEquipment(EquipmentProperties.__type_99_dive_bomber__);
        createEquipment(EquipmentProperties.__type_99_dive_bomber_egusa_squadron__);
        createEquipment(EquipmentProperties.__suisei__);
        createEquipment(EquipmentProperties.__suisei_model_12a__);
        createEquipment(EquipmentProperties.__suisei_model_22_634_air_group__);
        createEquipment(EquipmentProperties.__suisei_601_air_group__);
        createEquipment(EquipmentProperties.__suisei_egusa_squadron__);
        createEquipment(EquipmentProperties.__type_97_torpedo_bomber__);
        createEquipment(EquipmentProperties.__type_97_torpedo_bomber_tomonaga_squadron__);
        createEquipment(EquipmentProperties.__type_97_torpedo_bomber_murata_squadron__);
        createEquipment(EquipmentProperties.__tenzan__);
        createEquipment(EquipmentProperties.__tenzan_601_air_group__);
        createEquipment(EquipmentProperties.__tenzan_model_12_tomonaga_squadron__);
        createEquipment(EquipmentProperties.__tenzan_model_12_murata_squadron__);
        createEquipment(EquipmentProperties.__ryuusei__);
        createEquipment(EquipmentProperties.__ryuusei_kai__);
        createEquipment(EquipmentProperties.__ryuusei_kai_cardiv_1__);
        createEquipment(EquipmentProperties.__type_96_fighter__);
        createEquipment(EquipmentProperties.__type_0_fighter_model_21__);
        createEquipment(EquipmentProperties.__type_0_fighter_model_21_skilled__);
        createEquipment(EquipmentProperties.__type_0_fighter_model_32__);
        createEquipment(EquipmentProperties.__type_0_fighter_model_52_skilled__);
        createEquipment(EquipmentProperties.__zero_fighter_model_52c_601_air_group__);
        createEquipment(EquipmentProperties.__shiden_kai_2__);
        createEquipment(EquipmentProperties.__prototype_reppuu_late_model__);
        createEquipment(EquipmentProperties.__reppuu_model_11__);
        createEquipment(EquipmentProperties.__reppuu_kai_prototype_carrier_based_model__);
        createEquipment(EquipmentProperties.__reppuu_kai_2__);
        createEquipment(EquipmentProperties.__shinden_kai__);
        createEquipment(EquipmentProperties.__type_93_passive_sonar__);
        createEquipment(EquipmentProperties.__type_3_active_sonar__);
        createEquipment(EquipmentProperties.__type_94_depth_charge_projector__);
        createEquipment(EquipmentProperties.__type_3_depth_charge_projector__);
        createEquipment(EquipmentProperties.__type_95_depth_charge__);
        createEquipment(EquipmentProperties.__type_2_depth_charge__);
    }

    public static void createEquipment(EquipmentProperties equipmentProperties) {
        if (ALL_EQUIPMENTS.containsKey(equipmentProperties.getId())) {
            throw new IllegalStateException("ALL EQUIPMENT has the same id equipment already!");
        }
        ALL_EQUIPMENTS.put(equipmentProperties.getId(), new Equipment(equipmentProperties));
    }

    public static Equipment getEquipmentInstanceById(int uid, int level) {
        Equipment equipment = ALL_EQUIPMENTS.getOrDefault(uid, EMPTY);
        if (equipment.equals(Equipments.EMPTY)) {
            return EMPTY;
        }

        Equipment e = equipment.asCopy();
        e.setLevel(level);
        return e;
    }

    public static Equipment getEquipmentInstanceById(int uid) {
        Equipment equipment = ALL_EQUIPMENTS.getOrDefault(uid, EMPTY);
        if (equipment.equals(Equipments.EMPTY)) {
            return EMPTY;
        }

        Equipment e = equipment.asCopy();
        e.setLevel(0);
        return e;
    }

    public static Equipment getEmptyInstance() {
        return getEquipmentInstanceById(-1);
    }

    public static Component getEquipmentNameById(int id) {
        Equipment equipment = ALL_EQUIPMENTS.getOrDefault(id, EMPTY);

        return equipment.getName();
    }

}
