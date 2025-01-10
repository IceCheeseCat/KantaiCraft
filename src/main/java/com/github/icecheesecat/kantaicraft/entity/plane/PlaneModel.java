package com.github.icecheesecat.kantaicraft.entity.plane;

import com.github.icecheesecat.kantaicraft.entity.plane.fighter.EntityA6MZeroFighter;
import com.github.icecheesecat.kantaicraft.customObjects.ModEntity;
import net.minecraft.world.level.Level;

public enum PlaneModel {
    A6M_Zero;

    public BasicEntityPlane getEntity(Level level) {
        if (this == A6M_Zero) {
            return new EntityA6MZeroFighter(ModEntity.A6M_Zero_Fighter.get(), level);
        }

        return null;
    }
}
