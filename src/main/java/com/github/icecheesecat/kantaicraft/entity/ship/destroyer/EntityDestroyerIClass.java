package com.github.icecheesecat.kantaicraft.entity.ship.destroyer;

import com.github.icecheesecat.kantaicraft.entity.EntityID;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicDestroyerShip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class EntityDestroyerIClass extends BasicDestroyerShip {

    public EntityDestroyerIClass(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
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
    protected void initEquipments() {

    }

    @Override
    public EntityID getEntityId() {
        return EntityID.DestroyerIClass;
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
