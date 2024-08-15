package com.github.icecheesecat.shincolle.weaponary.canon;

import com.github.icecheesecat.shincolle.entity.util.ShipAttrs;

public class CANONS {

    public static final Canon TwelveCMSmallGunMount = Canon.Builder.aCanon()
            .withCanonAttr(new CanonAttr(ShipAttrs.Builder.aShipStatus().withFirePower(1).build(), 0))
            .withCanon_vel(10.0f)
            .withMaxCooldown(2*20)
            .withName("12cm small gun mount")
            .build();

}
