package com.github.icecheesecat.kantaicraft.entity.ship.destroyer;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.entity.ship.DestroyerShip;
import com.github.icecheesecat.kantaicraft.entity.stance.EntityStance;
import com.github.icecheesecat.kantaicraft.entity.stance.HostileStance;
import com.github.icecheesecat.kantaicraft.entity.stance.NeutralStance;
import com.github.icecheesecat.kantaicraft.entity.stance.PlayerStance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class DestroyerIClass extends DestroyerShip {

    public DestroyerIClass(EntityType<? extends PathfinderMob> entityType, Level level, EntityStance entityStance) {
        super(entityType, level, entityStance);
    }

    public static DestroyerIClass createPlayer(EntityType<? extends PathfinderMob> entityType, Level level) {
        return new DestroyerIClass(entityType, level, new PlayerStance());
    }

    public static DestroyerIClass createHostile(EntityType<? extends PathfinderMob> entityType, Level level) {
        return new DestroyerIClass(entityType, level, new HostileStance());
    }

    public static DestroyerIClass createNeutral(EntityType<? extends PathfinderMob> entityType, Level level) {
        return new DestroyerIClass(entityType, level, new NeutralStance());
    }

    @Override
    public double getPhysicalTurnRate() {
        return 0;
    }

    @Override
    public double getPhysicalSpeed() {
        return 0;
    }

    @Override
    protected void initEquipments(EquipmentHandler equipmentHandler) {

    }

    @Override
    public int getProcessTime() {
        return 200;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

}
