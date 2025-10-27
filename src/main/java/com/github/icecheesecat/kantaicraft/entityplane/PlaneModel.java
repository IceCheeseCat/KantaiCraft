package com.github.icecheesecat.kantaicraft.entityplane;

import net.minecraft.world.level.Level;

public enum PlaneModel {
    A6M_Zero;

    public BasicEntityPlane getEntity(Level level) {
        if (this == A6M_Zero) {
//            return new EntityA6MZeroFighter(ModEntity.A6M_Zero_Fighter.get(), level);
        }

        return null;
    }
}
