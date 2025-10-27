package com.github.icecheesecat.kantaicraft.tickable;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.github.icecheesecat.kantaicraft.tickable.attack.CannonShipAttack;

import javax.annotation.Nullable;
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

    @Nullable
    public CannonShipAttack getReadyCannonAction() {

        for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
            var equipment = equipmentHandler.getEquipments().get(i);
            if (equipment.isTypeOf(EquipmentType.SMALL_CANNON) ||
                    equipment.isTypeOf(EquipmentType.MEDIUM_CANNON) ||
                    equipment.isTypeOf(EquipmentType.LARGE_CANNON)) {

                if (!this.get(i).inCooldown()) {
                    return (CannonShipAttack) this.get(i);
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
