package com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.destroyer;

import com.github.icecheesecat.kantaicraft.entityship.entity.cannoship.DestroyerEntityShip;
import com.github.icecheesecat.kantaicraft.entityship.stance.HostileStance;
import com.github.icecheesecat.kantaicraft.entityship.stance.PlayerStance;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
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

    @Override
    protected void defaultEquipments(EquipmentHandler equipmentHandler) {
        equipmentHandler.setEquipment(0, "equipment_1", EquipmentManager.createNewEquipment(101), this);
        equipmentHandler.setEquipment(1, "equipment_2", EquipmentManager.createNewEquipment(102), this);
        equipmentHandler.setEquipment(2, "equipment_3", EquipmentManager.createNewEquipment(103), this);
        equipmentHandler.setEquipment(3, "equipment_4", EquipmentManager.createNewEquipment(104), this);
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

        @Override
        public EntityType<?> getPlayerSideEntityType() {
            return ModEntity.PlayerShip.AKATSUKI.get();
        }
    }

}
