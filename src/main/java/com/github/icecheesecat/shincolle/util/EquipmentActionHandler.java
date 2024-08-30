package com.github.icecheesecat.shincolle.util;

import com.github.icecheesecat.shincolle.entity.BasicEntityShip;
import com.github.icecheesecat.shincolle.util.tickable.ShipTickableAction;
import com.github.icecheesecat.shincolle.util.tickable.attack.ShipCannonAttack;
import com.github.icecheesecat.shincolle.equipment.Equipment;
import com.github.icecheesecat.shincolle.equipment.cannon.Cannon;
import com.github.icecheesecat.shincolle.equipment.EquipmentType;

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
        Equipment equipment = equipmentSlots.getSlot(i);
         switch (equipment.getWeaponType()) {
             case NULL -> this.set(i, ShipTickableAction.NULL);
             case CANNON -> this.set(i, new ShipCannonAttack(entityShip, ((Cannon) equipment)));
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
                if (equipmentSlots.equipmentTypes.get(i) == type) {
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
