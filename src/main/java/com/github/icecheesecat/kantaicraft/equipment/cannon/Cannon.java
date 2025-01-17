package com.github.icecheesecat.kantaicraft.equipment.cannon;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentLevel;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import com.github.icecheesecat.kantaicraft.equipment.IEquipmentCooldown;

public class Cannon extends Equipment implements IEquipmentCooldown {

    public Cannon(int uid, String name, double cooldown, double cannon_vel, double cannonRange, double size) {
        super(uid, name, EquipmentLevel.LEVEL_ONE);
        this.addStat(EquipmentStatType.CANNON_COOLDOWN, cooldown);
        this.addStat(EquipmentStatType.CANNON_MISSLE_VELOCITY, cannon_vel);
        this.addStat(EquipmentStatType.CANNON_RANGE, cannonRange);
        this.addStat(EquipmentStatType.CANNON_SIZE, size);
    }

    protected Cannon(Equipment equipment) {
        super(equipment);
    }

    @Override
    public double getCooldown() {
        if (this.stats.containsKey(EquipmentStatType.CANNON_COOLDOWN)) {
            return this.stats.get(EquipmentStatType.CANNON_COOLDOWN);
        }

        return -1.0d;
    }

    @Override
    public Equipment asCopy() {
        return new Cannon(this);
    }
}
