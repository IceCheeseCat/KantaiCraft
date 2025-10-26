package com.github.icecheesecat.kantaicraft.entity.ship.destroyer;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.entity.ship.DestroyerEntityShip;
import com.github.icecheesecat.kantaicraft.entity.stance.HostileStance;
import com.github.icecheesecat.kantaicraft.entity.stance.PlayerStance;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public abstract class Hibiki extends DestroyerEntityShip {
    public Hibiki(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void initEquipments(EquipmentHandler equipmentHandler) {
        equipmentHandler.setEquipment(0, Equipments.getEquipmentInstanceById(101), this);
    }

    @Override
    public int getProcessTime() {
        return 400;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    public static class PlayerSide extends Hibiki implements PlayerStance {
        public PlayerSide(EntityType<? extends PathfinderMob> entityType, Level level) {
            super(entityType, level);
        }
    }
    public static class HostileSide extends Hibiki implements HostileStance {
        public HostileSide(EntityType<? extends PathfinderMob> entityType, Level level) {
            super(entityType, level);
        }
    }

}
