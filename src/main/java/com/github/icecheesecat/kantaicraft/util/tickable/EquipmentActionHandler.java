package com.github.icecheesecat.kantaicraft.util.tickable;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import com.github.icecheesecat.kantaicraft.util.tickable.attack.ShipCannonAttack;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;

import java.util.ArrayList;
import java.util.List;

public class EquipmentActionHandler extends ArrayList<ShipTickableAction> {

    BasicEntityShip entityShip;
    EquipmentHandler equipmentHandler;

    public EquipmentActionHandler(BasicEntityShip entityShip, EquipmentHandler equipmentHandler) {
        this.entityShip = entityShip;
        this.equipmentHandler = equipmentHandler;
        for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
            this.add(ShipTickableAction.NULL);
        }

        resetAllActions();
    }

    public void resetAllActions() {
        for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
            this.updateAction(i);
        }
    }

    public void updateAction(int i) {
        Equipment equipment = equipmentHandler.getEquipments().get(i);
         switch (equipment.getType()) {
             case CANNON -> this.set(i, new ShipCannonAttack(entityShip, equipment, (int) equipment.getStat(EquipmentStatType.CANNON_COOLDOWN)));
             case NONE -> this.set(i, ShipTickableAction.NULL);
             default -> throw new RuntimeException("Unknown equipment type at " + entityShip);
         }
    }

    @Override
    public ShipTickableAction remove(int index) {
        return this.set(index, ShipTickableAction.NULL);
    }

    public List<ShipTickableAction> getActionsByWeaponType(EquipmentType type) {

        List<ShipTickableAction> ret = new ArrayList<>();
        for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
            if (equipmentHandler.getEquipments().get(i).getType() == type) {
                ShipTickableAction a = this.get(i);
                ret.add(a);
            }
        }
        return ret;
    }

    public ShipTickableAction getActionByWeaponTypeAndNotInCooldown(EquipmentType type) {

        for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
            if (equipmentHandler.getEquipments().get(i).getType() == type) {
                ShipTickableAction a = this.get(i);
                if (!a.inCooldown()) {
                    return a;
                }
            }
        }

        return null;
    }

    public void tick() {
        this.forEach(ShipTickableAction::tick);
    }

}
