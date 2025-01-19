package com.github.icecheesecat.kantaicraft.equipment;

public enum EquipmentType {

    NONE,
    CANNON,
    RADAR,
    PLANE;

    public static EquipmentType get(int ordinal) { return values()[ordinal]; }
    public static EquipmentType calculateType(int id) {
        switch (id/100) {
            case 1 -> {
                return EquipmentType.CANNON;
            }
            case 2 -> {
                return EquipmentType.PLANE;
            }
            default -> {
                return EquipmentType.NONE;
            }
        }
    }

}
