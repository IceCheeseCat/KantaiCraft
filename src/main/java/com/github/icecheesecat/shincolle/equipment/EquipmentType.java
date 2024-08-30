package com.github.icecheesecat.shincolle.equipment;

public enum EquipmentType {

    NULL,
    CANNON,
    PLANE;

    public static final EquipmentType values[] = values();
    public static EquipmentType get(int ordinal) { return values[ordinal]; }
}
