package com.github.icecheesecat.kantaicraft.equipment;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;

import java.util.Map;

public class EquipmentProperties {

    public static final EquipmentProperties EMPTY = new EquipmentProperties(-1, Component.translatable("equipment.empty"));
    public static final EquipmentProperties __12cm_single_gun_mount__ = new EquipmentProperties(101, Component.translatable("cannon.12cm_single_gun_mount"));
    public static final EquipmentProperties __12cm_twin_gun_mount__ = new EquipmentProperties(102, Component.translatable("cannon.12cm_twin_gun_mount"));
    public static final EquipmentProperties __12cm_twin_gun_mount_model_b_kai_2__ = new EquipmentProperties(103, Component.translatable("cannon.12cm_twin_gun_mount_model_b_kai_2"));

    public static Map<Integer, EquipmentProperties> ALL_PROPERTIES = ImmutableMap.of(
            -1, EMPTY,
            101, __12cm_single_gun_mount__,
            102, __12cm_twin_gun_mount__,
            103, __12cm_twin_gun_mount_model_b_kai_2__
    );

    private final int id;
    private final Component name;

    private EquipmentProperties(int id, Component name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name.getString();
    }

    public Component getComponentName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

}
