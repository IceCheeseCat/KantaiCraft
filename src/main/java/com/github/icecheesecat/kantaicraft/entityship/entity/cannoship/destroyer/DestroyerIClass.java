package com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.destroyer;

import com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.DestroyerEntityShip;
import com.github.icecheesecat.kantaicraft.entityship.stance.HostileStance;
import com.github.icecheesecat.kantaicraft.entityship.stance.PlayerStance;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public abstract class DestroyerIClass extends DestroyerEntityShip {

    public DestroyerIClass(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defaultEquipments(EquipmentHandler equipmentHandler) {

    }

    @Override
    public int getProcessTime() {
        return 200;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    public static class PlayerSide extends DestroyerIClass implements PlayerStance {
        public PlayerSide(EntityType<? extends PathfinderMob> entityType, Level level) {
            super(entityType, level);
        }
    }
    public static class HostileSide extends DestroyerIClass implements HostileStance {
        public HostileSide(EntityType<? extends PathfinderMob> entityType, Level level) {
            super(entityType, level);
        }

        @Override
        public EntityType<?> getPlayerSideEntityType() {
            return null; // TODO
        }
    }

}
