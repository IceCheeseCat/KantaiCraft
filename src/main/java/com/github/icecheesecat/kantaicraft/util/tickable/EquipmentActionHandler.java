package com.github.icecheesecat.kantaicraft.util.tickable;

import com.github.icecheesecat.kantaicraft.common.EquipmentManager;
import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.github.icecheesecat.kantaicraft.util.tickable.attack.ShipCannonAttack;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.cannon.Cannon;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;

import java.util.ArrayList;
import java.util.List;

public class EquipmentActionHandler extends ArrayList<ShipTickableAction> {

    BasicEntityShip entityShip;
    EquipmentSlots equipmentSlots;

    public EquipmentActionHandler(BasicEntityShip entityShip, EquipmentSlots equipmentSlots) {
        this.entityShip = entityShip;
        this.equipmentSlots = equipmentSlots;
        for (int i = 0; i < equipmentSlots.getSlotSize(); i++) {
            this.add(ShipTickableAction.NULL);
        }

        resetAllActions();
    }

    public void resetAllActions() {
        for (int i = 0; i < equipmentSlots.getSlotSize(); i++) {
            this.resetAction(i);
        }
    }

    public void resetAction(int i) {
        Equipment equipment = equipmentSlots.getEquipments().get(i);
         switch (equipment.getType()) {
             case CANNON -> this.set(i, new ShipCannonAttack(entityShip, ((Cannon) equipment)));
             case NONE -> this.set(i, ShipTickableAction.NULL);
             default -> throw new RuntimeException("Unknown equipment type at " + entityShip.getCustomShipName());
         }
    }

    @Override
    public ShipTickableAction remove(int index) {
        return this.set(index, ShipTickableAction.NULL);
    }

    public List<ShipTickableAction> getActionsByWeaponType(EquipmentType type) {
        if (this.contains(type)) {
            List<ShipTickableAction> ret = new ArrayList<>();
            for (int i = 0; i < equipmentSlots.getSlotSize(); i++) {
                if (equipmentSlots.getEquipmentTypes().get(i) == type) {
                    ShipTickableAction a = this.get(i);
                    ret.add(a);
                }
            }
            return ret;
        }

        return new ArrayList<>();
    }

    public void tick() {
        this.forEach(ShipTickableAction::tick);
    }

}
