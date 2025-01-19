package com.github.icecheesecat.kantaicraft.entity.ship;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;

import java.util.List;
public interface IEquipmentSelector {

    List<Equipment> evaluateEquipments(List<Equipment> equipments);

}
