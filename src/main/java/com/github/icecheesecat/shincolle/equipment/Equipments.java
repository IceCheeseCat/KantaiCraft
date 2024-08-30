package com.github.icecheesecat.shincolle.equipment;

public class Equipments {
    public static final Equipment TwelveCMSingleGunMount = new Equipment(EquipmentType.CANNON, 101, EquipmentLevel.NULL);

    public static Equipment gettter(int uid) {
        if (uid == 101) {
            return TwelveCMSingleGunMount;
        }
        return Equipment.EMPTY;
    }

}
