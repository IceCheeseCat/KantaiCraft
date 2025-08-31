package com.github.icecheesecat.kantaicraft.util.tickable;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import com.github.icecheesecat.kantaicraft.util.tickable.attack.ShipCannonAttack;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;

import java.util.ArrayList;
import java.util.List;

public class EquipmentActionHandler extends ArrayList<ShipTickableAction> {

    EntityShip entityShip;
    EquipmentHandler equipmentHandler;

    public EquipmentActionHandler(EntityShip entityShip, EquipmentHandler equipmentHandler) {
        this.entityShip = entityShip;
        this.equipmentHandler = equipmentHandler;
        for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
            this.add(ShipTickableAction.NULL);
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

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).isEmpty()) continue;
            string += i + ">>" + this.get(i) + "\n";
        }

        return string;
    }
}
