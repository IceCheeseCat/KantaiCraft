package com.github.icecheesecat.shincolle.equipment.cannon;

import com.github.icecheesecat.shincolle.util.ShipAttrs;

public class CANNONS {

    public static final Cannon TwelveCMSmallGunMount = Cannon.CannonBuilder.aCannon()
            .withName("12cm_small_gun_mount")
            .withUid(101)
            .withCannonAttr(new CannonAttr(ShipAttrs.Builder.aShipStatus().withFirePower(1).build(), 0))
            .withCannon_vel(100.0f)
            .withCannon_range(20.0f)
            .withMaxCooldown(2*20)
            .build();

}
