package com.github.icecheesecat.kantaicraft.equipment;

public enum EquipmentType {

    NONE,
    CANNON,
    RADAR,
    PLANE;

    public static EquipmentType get(int ordinal) { return values()[ordinal]; }
}
