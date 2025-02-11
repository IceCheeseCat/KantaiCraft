package com.github.icecheesecat.kantaicraft.equipment;

import com.github.icecheesecat.kantaicraft.config.ConfigEquipmentStats;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class Equipments {

//    public static final DeferredRegister<Equipment> EQUIPMENTS = DeferredRegister.create(new ResourceLocation(KantaiCraft.MODID, "deferredregister/equipment"), KantaiCraft.MODID);
//    public static final Supplier<IForgeRegistry<Equipment>> REGISTRY_SUPPLIER = EQUIPMENTS.makeRegistry(RegistryBuilder::new);
    public static final Equipment EMPTY = new Equipment(EquipmentProperties.EMPTY.getId(), EquipmentProperties.EMPTY.getComponentName(), EquipmentType.NONE, ImmutableMap.of()) {
        @Override
        public Equipment asCopy() {
            return null;
        }

        @Override
        protected void setRequiredStats() {
            this.requiredStats = ImmutableList.of();
        }
    };
    public static Equipment __12cm_single_gun_mount__;
    public static Equipment __12cm_twin_gun_mount__;
    public static Equipment __12cm_twin_gun_mount_model_B_kai_2__;


    public static final Map<Integer, Equipment> ALL_EQUIPMENTS = new HashMap<>();

    static {
        __12cm_single_gun_mount__ = new CannonEquipment(EquipmentProperties.__12cm_single_gun_mount__.getId(), EquipmentProperties.__12cm_twin_gun_mount__.getComponentName(), ImmutableMap.of());
        __12cm_twin_gun_mount__ = new CannonEquipment(EquipmentProperties.__12cm_twin_gun_mount__.getId(), EquipmentProperties.__12cm_twin_gun_mount__.getComponentName(), ImmutableMap.of());
        __12cm_twin_gun_mount_model_B_kai_2__ = new CannonEquipment(EquipmentProperties.__12cm_twin_gun_mount_model_b_kai_2__.getId(), EquipmentProperties.__12cm_twin_gun_mount_model_b_kai_2__.getComponentName(), ImmutableMap.of());

        ALL_EQUIPMENTS.put(-1, EMPTY);
        ALL_EQUIPMENTS.put(101, __12cm_single_gun_mount__);
        ALL_EQUIPMENTS.put(102, __12cm_twin_gun_mount__);
        ALL_EQUIPMENTS.put(103, __12cm_twin_gun_mount_model_B_kai_2__);
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

    public static Component getEquipmentNameById(int id) {
        Equipment equipment = ALL_EQUIPMENTS.getOrDefault(id, EMPTY);

        return equipment.getName();
    }

}
