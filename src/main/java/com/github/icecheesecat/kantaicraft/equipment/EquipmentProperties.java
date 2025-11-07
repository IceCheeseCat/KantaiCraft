package com.github.icecheesecat.kantaicraft.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.INBTSerializable;

public class EquipmentProperties implements INBTSerializable<CompoundTag> {

    public static final EquipmentProperties EMPTY = new EquipmentProperties(-1, Component.translatable("equipment.empty"), EquipmentClass.NONE);
    public static final EquipmentProperties __12cm_single_gun_mount__ = new EquipmentProperties(101, Component.translatable("12cm_single_gun_mount"), EquipmentClass.SMALL_CANNON);
    public static final EquipmentProperties __12cm_twin_gun_mount__ = new EquipmentProperties(102, Component.translatable("12cm_twin_gun_mount"), EquipmentClass.SMALL_CANNON);
    public static final EquipmentProperties __12cm_twin_gun_mount_model_b_kai_2__ = new EquipmentProperties(103, Component.translatable("12cm_twin_gun_mount_model_b_kai_2"), EquipmentClass.SMALL_CANNON);
    public static final EquipmentProperties __14cm_single_gun_mount__ = new EquipmentProperties(201, Component.translatable("14cm_single_gun_mount"), EquipmentClass.MEDIUM_CANNON);
    public static final EquipmentProperties __155mm_triple_gun_mount__ = new EquipmentProperties(202, Component.translatable("155mm_triple_gun_mount"), EquipmentClass.MEDIUM_CANNON);
    public static final EquipmentProperties __203mm_twin_gun_mount__ = new EquipmentProperties(203, Component.translatable("203mm_twin_gun_mount"), EquipmentClass.MEDIUM_CANNON);
    public static final EquipmentProperties __203mm_no3_single_gun_mount__ = new EquipmentProperties(204, Component.translatable("203mm_no3_twin_gun_mount"), EquipmentClass.MEDIUM_CANNON);
    public static final EquipmentProperties __prototype_203mm_no4_single_gun_mount__ = new EquipmentProperties(205, Component.translatable("203mm_no4_twin_gun_mount"), EquipmentClass.MEDIUM_CANNON);
    public static final EquipmentProperties __356mm_twin_gun_mount__ = new EquipmentProperties(301, Component.translatable("356mm_twin_gun_mount"), EquipmentClass.LARGE_CANNON);
    public static final EquipmentProperties __prototype_356mm_triple_gun_mount__ = new EquipmentProperties(302, Component.translatable("prototype_356mm_twin_gun_mount"), EquipmentClass.LARGE_CANNON);
    public static final EquipmentProperties __356mm_twin_gun_mount_kai_2__ = new EquipmentProperties(303, Component.translatable("356mm_twin_gun_mount_kai_2"), EquipmentClass.LARGE_CANNON);
    public static final EquipmentProperties __41cm_twin_gun_mount__ = new EquipmentProperties(304, Component.translatable("41cm_twin_gun_mount"), EquipmentClass.LARGE_CANNON);
    public static final EquipmentProperties __prototype_41cm_triple_gun_mount__ = new EquipmentProperties(305, Component.translatable("prototype_41cm_twin_gun_mount"), EquipmentClass.LARGE_CANNON);
    public static final EquipmentProperties __41cm_triple_gun_mount_kai__ = new EquipmentProperties(306, Component.translatable("41cm_triple_gun_mount_kai"), EquipmentClass.LARGE_CANNON);
    public static final EquipmentProperties __41cm_triple_gun_mount_kai_2__ = new EquipmentProperties(307, Component.translatable("41cm_triple_gun_mount_kai_2"), EquipmentClass.LARGE_CANNON);
    public static final EquipmentProperties __61cm_triple_torpedo_mount__ = new EquipmentProperties(401, Component.translatable("61cm_triple_torpedo_mount"), EquipmentClass.TORPEDO);
    public static final EquipmentProperties __61cm_triple_oxygen_torpedo_mount__ = new EquipmentProperties(402, Component.translatable("61cm_triple_oxygen_torpedo_mount"), EquipmentClass.TORPEDO);
    public static final EquipmentProperties __61cm_quadruple_torpedo_mount__ = new EquipmentProperties(403, Component.translatable("61cm_quadruple_torpedo_mount"), EquipmentClass.TORPEDO);
    public static final EquipmentProperties __61cm_quadruple_oxygen_torpedo_mount__ = new EquipmentProperties(404, Component.translatable("61cm_quadruple_oxygen_torpedo_mount"), EquipmentClass.TORPEDO);
    public static final EquipmentProperties __61cm_quintuple_oxygen_torpedo_mount__ = new EquipmentProperties(405, Component.translatable("61cm_quintuple_oxygen_torpedo_mount"), EquipmentClass.TORPEDO);
    public static final EquipmentProperties __type_13_air_radar__ = new EquipmentProperties(501, Component.translatable("type_13_air_radar"), EquipmentClass.RADAR);
    public static final EquipmentProperties __type_13_air_radar_kai__ = new EquipmentProperties(502, Component.translatable("type_13_air_radar_kai"), EquipmentClass.RADAR);
    public static final EquipmentProperties __type_22_surface_radar__ = new EquipmentProperties(503, Component.translatable("type_22_surface_radar"), EquipmentClass.RADAR);
    public static final EquipmentProperties __type_22_surface_radar_kai_4__ = new EquipmentProperties(504, Component.translatable("type_22_surface_radar_kai_4"), EquipmentClass.RADAR);
    public static final EquipmentProperties __type_33_surface_radar__ = new EquipmentProperties(505, Component.translatable("type_33_surface_radar"), EquipmentClass.RADAR);
    public static final EquipmentProperties __type_21_air_radar__ = new EquipmentProperties(506, Component.translatable("type_21_air_radar"), EquipmentClass.RADAR);
    public static final EquipmentProperties __type_21_air_radar_kai__ = new EquipmentProperties(507, Component.translatable("type_21_air_radar_kai"), EquipmentClass.RADAR);
    public static final EquipmentProperties __type_32_surface_radar__ = new EquipmentProperties(508, Component.translatable("type_32_surface_radar"), EquipmentClass.RADAR);
    public static final EquipmentProperties __type_42_air_radar__ = new EquipmentProperties(509, Component.translatable("type_42_air_radar"), EquipmentClass.RADAR);
    public static final EquipmentProperties __type_0_reconnaissance_seaplane__ = new EquipmentProperties(601, Component.translatable("type_0_reconnaissance_seaplane"), EquipmentClass.SEAPLANE_RECON);
    public static final EquipmentProperties __type_0_reconnaissance_seaplane_model_11b__ = new EquipmentProperties(602, Component.translatable("type_0_reconnaissance_seaplane_model_11b"), EquipmentClass.SEAPLANE_RECON);
    public static final EquipmentProperties __type_0_reconnaissance_seaplane_model_11b_skilled__ = new EquipmentProperties(603, Component.translatable("type_0_reconnaissance_seaplane_model_11b_skilled"), EquipmentClass.SEAPLANE_RECON);
    public static final EquipmentProperties __type_0_observation_seaplane__ = new EquipmentProperties(604, Component.translatable("type_0_observation_seaplane"), EquipmentClass.SEAPLANE_RECON);
    public static final EquipmentProperties __type_98_reconnaissance_seaplane_night_recon__ = new EquipmentProperties(605, Component.translatable("type_98_reconnaissance_seaplane_night_recon"), EquipmentClass.SEAPLANE_RECON);
    public static final EquipmentProperties __type_0_reconnaissance_seaplane_model_11b_kai_night_recon__ = new EquipmentProperties(606, Component.translatable("type_0_reconnaissance_seaplane_model_11b_kai_night_recon"), EquipmentClass.SEAPLANE_RECON);
    public static final EquipmentProperties __type_2_seaplane_fighter_kai__ = new EquipmentProperties(701, Component.translatable("type_2_seaplane_fighter_kai"), EquipmentClass.SEAPLANE_FIGHTER);
    public static final EquipmentProperties __kyoufuu_kai__ = new EquipmentProperties(702, Component.translatable("kyoufuu_kai"), EquipmentClass.SEAPLANE_FIGHTER);
    public static final EquipmentProperties __type_99_dive_bomber__ = new EquipmentProperties(801, Component.translatable("type_99_dive_bomber"), EquipmentClass.AIRCRAFT_DIVE_BOMBER);
    public static final EquipmentProperties __type_99_dive_bomber_egusa_squadron__ = new EquipmentProperties(802, Component.translatable("type_99_dive_bomber_egusa_squadron"), EquipmentClass.AIRCRAFT_DIVE_BOMBER);
    public static final EquipmentProperties __suisei__ = new EquipmentProperties(803, Component.translatable("suisei"), EquipmentClass.AIRCRAFT_DIVE_BOMBER);
    public static final EquipmentProperties __suisei_model_12a__ = new EquipmentProperties(804, Component.translatable("suisei_model_12a"), EquipmentClass.AIRCRAFT_DIVE_BOMBER);
    public static final EquipmentProperties __suisei_model_22_634_air_group__ = new EquipmentProperties(805, Component.translatable("suisei_model_22_634_air_group"), EquipmentClass.AIRCRAFT_DIVE_BOMBER);
    public static final EquipmentProperties __suisei_601_air_group__ = new EquipmentProperties(806, Component.translatable("suisei_601_air_group"), EquipmentClass.AIRCRAFT_DIVE_BOMBER);
    public static final EquipmentProperties __suisei_egusa_squadron__ = new EquipmentProperties(807, Component.translatable("suisei_egusa_squadron"), EquipmentClass.AIRCRAFT_DIVE_BOMBER);
    public static final EquipmentProperties __type_97_torpedo_bomber__ = new EquipmentProperties(901, Component.translatable("type_97_torpedo_bomber"), EquipmentClass.AIRCRAFT_TORPEDO_BOMBER);
    public static final EquipmentProperties __type_97_torpedo_bomber_tomonaga_squadron__ = new EquipmentProperties(902, Component.translatable("type_97_torpedo_bomber_tomonaga_squadron"), EquipmentClass.AIRCRAFT_TORPEDO_BOMBER);
    public static final EquipmentProperties __type_97_torpedo_bomber_murata_squadron__ = new EquipmentProperties(903, Component.translatable("type_97_torpedo_bomber_murata_squadron"), EquipmentClass.AIRCRAFT_TORPEDO_BOMBER);
    public static final EquipmentProperties __tenzan__ = new EquipmentProperties(904, Component.translatable("tenzan"), EquipmentClass.AIRCRAFT_TORPEDO_BOMBER);
    public static final EquipmentProperties __tenzan_601_air_group__ = new EquipmentProperties(905, Component.translatable("tenzan_601_air_group"), EquipmentClass.AIRCRAFT_TORPEDO_BOMBER);
    public static final EquipmentProperties __tenzan_model_12_tomonaga_squadron__ = new EquipmentProperties(906, Component.translatable("tenzan_model_12_tomonaga_squadron"), EquipmentClass.AIRCRAFT_TORPEDO_BOMBER);
    public static final EquipmentProperties __tenzan_model_12_murata_squadron__ = new EquipmentProperties(907, Component.translatable("tenzan_model_12_murata_squadron"), EquipmentClass.AIRCRAFT_TORPEDO_BOMBER);
    public static final EquipmentProperties __ryuusei__ = new EquipmentProperties(908, Component.translatable("ryuusei"), EquipmentClass.AIRCRAFT_TORPEDO_BOMBER);
    public static final EquipmentProperties __ryuusei_kai__ = new EquipmentProperties(909, Component.translatable("ryuusei_kai"), EquipmentClass.AIRCRAFT_TORPEDO_BOMBER);
    public static final EquipmentProperties __ryuusei_kai_cardiv_1__ = new EquipmentProperties(910, Component.translatable("ryuusei_kai_cardiv_1"), EquipmentClass.AIRCRAFT_TORPEDO_BOMBER);
    public static final EquipmentProperties __type_96_fighter__ = new EquipmentProperties(1001, Component.translatable("type_96_fighter"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __type_0_fighter_model_21__ = new EquipmentProperties(1002, Component.translatable("type_0_fighter_model_21"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __type_0_fighter_model_21_skilled__ = new EquipmentProperties(1003, Component.translatable("type_0_fighter_model_21_skilled"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __type_0_fighter_model_32__ = new EquipmentProperties(1004, Component.translatable("type_0_fighter_model_32"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __type_0_fighter_model_52_skilled__ = new EquipmentProperties(1005, Component.translatable("type_0_fighter_model_52_skilled"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __zero_fighter_model_52c_601_air_group__ = new EquipmentProperties(1006, Component.translatable("zero_fighter_model_52c_601_air_group"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __shiden_kai_2__ = new EquipmentProperties(1007, Component.translatable("shiden_kai_2"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __prototype_reppuu_late_model__ = new EquipmentProperties(1008, Component.translatable("prototype_reppuu_late_model"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __reppuu_model_11__ = new EquipmentProperties(1009, Component.translatable("reppuu_model_11"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __reppuu_kai_prototype_carrier_based_model__ = new EquipmentProperties(1010, Component.translatable("reppuu_kai_prototype_carrier_based_model"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __reppuu_kai_2__ = new EquipmentProperties(1011, Component.translatable("reppuu_kai_2"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __shinden_kai__ = new EquipmentProperties(1012, Component.translatable("shinden_kai"), EquipmentClass.AIRCRAFT_FIGHTER);
    public static final EquipmentProperties __type_93_passive_sonar__ = new EquipmentProperties(1101, Component.translatable("type_93_passive_sonar"), EquipmentClass.ANTI_SUBMARINE);
    public static final EquipmentProperties __type_3_active_sonar__ = new EquipmentProperties(1102, Component.translatable("type_3_active_sonar"), EquipmentClass.ANTI_SUBMARINE);
    public static final EquipmentProperties __type_94_depth_charge_projector__ = new EquipmentProperties(1103, Component.translatable("type_94_depth_charge_projector"), EquipmentClass.ANTI_SUBMARINE);
    public static final EquipmentProperties __type_3_depth_charge_projector__ = new EquipmentProperties(1104, Component.translatable("type_3_depth_charge_projector"), EquipmentClass.ANTI_SUBMARINE);
    public static final EquipmentProperties __type_95_depth_charge__ = new EquipmentProperties(1105, Component.translatable("type_95_depth_charge"), EquipmentClass.ANTI_SUBMARINE);
    public static final EquipmentProperties __type_2_depth_charge__ = new EquipmentProperties(1106, Component.translatable("type_2_depth_charge"), EquipmentClass.ANTI_SUBMARINE);

    private int id;
    private Component name;
    private EquipmentClass equipmentClass;

    public EquipmentProperties() {
    }

    public EquipmentProperties(int id, Component name, EquipmentClass equipmentClass) {
        this.id = id;
        this.name = name;
        this.equipmentClass = equipmentClass;
    }

    public String getString() {
        return this.name.getString();
    }

    public Component getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public EquipmentClass getEquipmentType() {
        return this.equipmentClass;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("id", this.id);
        nbt.putInt("equipment_type", this.equipmentClass.ordinal());
        nbt.putString("name", Component.Serializer.toJson(name));

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.id = nbt.getInt("id");
        this.equipmentClass = EquipmentClass.get(nbt.getInt("equipment_type"));
        this.name = Component.Serializer.fromJson(nbt.getString("name"));
    }
}
