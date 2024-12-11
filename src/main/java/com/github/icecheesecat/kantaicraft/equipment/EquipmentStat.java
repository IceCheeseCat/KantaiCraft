package com.github.icecheesecat.kantaicraft.equipment;

public class EquipmentStat {

    public String name;
    public int value;

    public EquipmentStat(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static final String FirePower = "firepower";
    public static final String Torpedo = "torpedo";
    public static final String CannonSize = "cannon_size";
    public static final String CannonRange = "cannon_range";

}
