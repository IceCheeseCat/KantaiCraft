package com.github.icecheesecat.kantaicraft.entityship.entity;

public enum ShipClass {
    NONE,
    DESTROYER,
    LIGHT_CRUISER,
    HEAVY_CRUISER,
    BATTLESHIP,
    AVIATION_BATTLESHIP,
    LIGHT_AIRCRAFT_CARRIER,
    AIRCRAFT_CARRIER;

    public static ShipClass get(int i) {
        return values()[i];
    }

}
