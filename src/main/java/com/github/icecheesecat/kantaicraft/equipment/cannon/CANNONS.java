package com.github.icecheesecat.kantaicraft.equipment.cannon;

import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;

public class CANNONS {

    public static final Cannon TwelveCMSmallGunMount = Cannon.CannonBuilder.aCannon()
            .withName("12cm_small_gun_mount")
            .withUid(101)
            .withCannonAttr(new CannonStats(0).addStats(ModShipAttributes.FIREPOWER.get(), 1.0d))
            .withCannon_vel(100.0f)
            .withCannon_range(20.0f)
            .withMaxCooldown(2*20)
            .build();

}
