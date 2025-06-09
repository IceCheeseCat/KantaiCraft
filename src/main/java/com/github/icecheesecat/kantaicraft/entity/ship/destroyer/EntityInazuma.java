package com.github.icecheesecat.kantaicraft.entity.ship.destroyer;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicDestroyerShip;
import com.github.icecheesecat.kantaicraft.util.ShipFields;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class EntityInazuma extends BasicDestroyerShip {

    public EntityInazuma(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void initEquipments() {

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
    public ShipFields.ShipName getShipName() {
        return ShipFields.ShipName.Inazuma;
    }
}
