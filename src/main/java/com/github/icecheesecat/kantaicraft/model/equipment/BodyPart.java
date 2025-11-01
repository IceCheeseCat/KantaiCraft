package com.github.icecheesecat.kantaicraft.model.equipment;

import net.minecraftforge.common.util.INBTSerializable;
import org.joml.Vector3d;

public enum BodyPart {
    none,
    right_arm,
    left_arm,
    back,
    right_leg,
    left_leg;

    public record Position(Vector3d bodyPartPosition, Vector3d partOffsetToWeapon) {

    }
}
