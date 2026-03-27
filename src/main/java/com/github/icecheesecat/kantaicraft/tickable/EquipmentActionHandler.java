package com.github.icecheesecat.kantaicraft.tickable;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentClass;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.exception.KantaiCraftException;
import com.github.icecheesecat.kantaicraft.tickable.attack.CannonAttack;

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
            this.add(evaluateAction(entityShip, equipmentHandler.getEquipment(i)));
        }
    }

    public ShipTickableAction evaluateAction(EntityShip entityShip, Equipment equipment) {
        return switch (equipment.getEquipmentClass()) {
            case SMALL_CANNON -> new CannonAttack(entityShip, equipment);
            case NONE -> ShipTickableAction.NULL;
            default -> throw new KantaiCraftException(this.getClass(), "not implemented ship action");
        };
    }

    @Override
    public ShipTickableAction remove(int index) {
        return this.set(index, ShipTickableAction.NULL);
    }

    public List<ShipTickableAction> getActionsByWeaponType(EquipmentClass type) {

        List<ShipTickableAction> ret = new ArrayList<>();
        for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
            if (equipmentHandler.getEquipments().get(i).getEquipmentClass() == type) {
                ShipTickableAction a = this.get(i);
                ret.add(a);
            }
        }
        return ret;
    }

    @Nullable
    public CannonAttack getReadyCannonAction() {

        for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
            var equipment = equipmentHandler.getEquipments().get(i);
            if (equipment.isTypeOf(EquipmentClass.SMALL_CANNON) ||
                    equipment.isTypeOf(EquipmentClass.MEDIUM_CANNON) ||
                    equipment.isTypeOf(EquipmentClass.LARGE_CANNON)) {

                if (!this.get(i).inCooldown()) {
                    return (CannonAttack) this.get(i);
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
