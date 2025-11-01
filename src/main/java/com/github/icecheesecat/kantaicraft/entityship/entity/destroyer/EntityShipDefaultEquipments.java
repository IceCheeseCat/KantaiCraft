package com.github.icecheesecat.kantaicraft.entityship.entity.destroyer;

import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.equipment.handler.ArmedEquipment;

import java.util.Map;
import java.util.function.Supplier;

public class EntityShipDefaultEquipments {
    public static final String equipment_1 = "equipment_1";
    public static final String equipment_2 = "equipment_2";
    public static final String equipment_3 = "equipment_3";
    public static final String equipment_4 = "equipment_4";
    public static final String equipment_5 = "equipment_5";

    public static final Map<Integer, Supplier<ArmedEquipment>> DESTROYER = Map.of(0, ()-> new ArmedEquipment(EquipmentManager.createNewEquipment(101, 0), equipment_1));
}
