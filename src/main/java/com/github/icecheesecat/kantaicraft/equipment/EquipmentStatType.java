package com.github.icecheesecat.kantaicraft.equipment;

public enum EquipmentStatType {
    FIREPOWER,
    TORPEDO,
    ANTIAIR,
    CANNON_SIZE,
    CANNON_RANGE,
    CANNON_COOLDOWN,
    CANNON_MISSLE_VELOCITY;

    public static EquipmentStatType get(int i) {
        return values()[i];
    }
}
