package com.github.icecheesecat.kantaicraft.equipment;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;

import java.util.Map;

public class CannonEquipment extends Equipment {

    protected CannonEquipment(CannonEquipment cannonEquipment) {
        super(cannonEquipment);
    }

    public CannonEquipment(int id, Component name, Map<EquipmentStatType, Double> stats) {
        super(id, name, EquipmentType.CANNON, stats);
    }


    @Override
    public Equipment asCopy() {
        return new CannonEquipment(this);
    }

    @Override
    protected void setRequiredStats() {
        this.requiredStats = ImmutableList.of(EquipmentStatType.CANNON_SIZE, EquipmentStatType.CANNON_RANGE, EquipmentStatType.CANNON_COOLDOWN, EquipmentStatType.CANNON_MISSLE_VELOCITY, EquipmentStatType.FIREPOWER);
    }
}
