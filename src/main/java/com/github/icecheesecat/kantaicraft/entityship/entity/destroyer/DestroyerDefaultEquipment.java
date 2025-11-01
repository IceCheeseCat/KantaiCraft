package com.github.icecheesecat.kantaicraft.entityship.entity.destroyer;

import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.equipment.handler.ArmedEquipment;
import com.github.icecheesecat.kantaicraft.model.equipment.BodyPart;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DestroyerDefaultEquipment {
    public static final Map<Integer, Supplier<ArmedEquipment>> DESTROYER = Map.of(0, ()-> new ArmedEquipment(EquipmentManager.createNewEquipment(101, 0), BodyPart.right_arm));

}
