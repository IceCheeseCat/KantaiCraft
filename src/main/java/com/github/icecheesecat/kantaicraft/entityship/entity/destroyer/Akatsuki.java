package com.github.icecheesecat.kantaicraft.entityship.entity.destroyer;

import com.github.icecheesecat.kantaicraft.entityship.entity.DestroyerEntityShip;
import com.github.icecheesecat.kantaicraft.entityship.stance.HostileStance;
import com.github.icecheesecat.kantaicraft.entityship.stance.PlayerStance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public abstract class Akatsuki extends DestroyerEntityShip {
    public Akatsuki(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public int getProcessTime() {
        return 400;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    public static class PlayerSide extends Akatsuki implements PlayerStance {
        public PlayerSide(EntityType<? extends PathfinderMob> entityType, Level level) {
            super(entityType, level);
        }
    }
    public static class HostileSide extends Akatsuki implements HostileStance {
        public HostileSide(EntityType<? extends PathfinderMob> entityType, Level level) {
            super(entityType, level);
        }
    }

}
