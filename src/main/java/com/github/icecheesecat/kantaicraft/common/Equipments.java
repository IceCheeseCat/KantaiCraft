package com.github.icecheesecat.kantaicraft.common;

import com.github.icecheesecat.kantaicraft.equipment.EquipmentStat;
import com.github.icecheesecat.kantaicraft.equipment.cannon.Cannon;

public class Equipments {

    public static final Cannon TwelveCMSmallGunMount = new Cannon(101, "12cm_small_gun_mount", 2*20, 100.0f, 20.0f, 1) {
        @Override
        public void setupStats() {
            this.addStat(new EquipmentStat(EquipmentStat.FirePower, 1));
        }
    };


}
