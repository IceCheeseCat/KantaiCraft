package com.github.icecheesecat.kantaicraft.entity.ship.destroyer;

import com.github.icecheesecat.kantaicraft.entity.EntityID;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicDestroyerShip;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class EntityInazuma extends BasicDestroyerShip {

    public EntityInazuma(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void initEquipments() {

    }

    @Override
    public EntityID getEntityId() {
        return EntityID.Inazuma;
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
    public double getPhysicalTurnRate() {
        return 0;
    }

    @Override
    public double getPhysicalSpeed() {
        return 0;
    }

}
