package com.github.icecheesecat.kantaicraft.util;

public class ShipFields {
    public enum ShipClass {
        DESTROYER,
        CARRIER;

        public static ShipClass getEnum(int i) {
            return values()[i];
        }
    }

    public enum ShipName {
        DestroyerRo;

        public static ShipName getEnum(int i) {
            return values()[i];
        }
    }
}
