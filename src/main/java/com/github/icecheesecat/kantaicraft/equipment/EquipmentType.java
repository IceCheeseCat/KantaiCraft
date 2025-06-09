package com.github.icecheesecat.kantaicraft.equipment;

/**
 *
 */
public enum EquipmentType {

    NONE,
    SMALL_CANNON,
    MEDIUM_CANNON,
    LARGE_CANNON,
    TORPEDO,
    RADAR,
    SEAPLANE_RECON,
    SEAPLANE_FIGHTER,
    AIRCRAFT_DIVE_BOMBER,
    AIRCRAFT_TORPEDO_BOMBER,
    AIRCRAFT_FIGHTER,
    ANTI_SUBMARINE,
    OTHER;

    public static EquipmentType get(int ordinal) { return values()[ordinal]; }

}
