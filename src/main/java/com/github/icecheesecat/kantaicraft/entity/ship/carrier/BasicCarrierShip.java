package com.github.icecheesecat.kantaicraft.entity.ship.carrier;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.entity.ship.brain.CarrierBrain;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

public abstract class BasicCarrierShip extends BasicEntityShip {

    protected BasicCarrierShip(EntityType<? extends PathfinderMob> entityType, Level level, EquipmentSlots equipmentSlot) {
        super(entityType, level, equipmentSlot);
    }

    @Override
    public Brain<BasicCarrierShip> getBrain() {
        return (Brain<BasicCarrierShip>) this.brain;
    }

    @Override
    protected Brain<BasicCarrierShip> makeBrain(Dynamic<?> dyn) {
        return CarrierBrain.makeBrain(dyn);
    }
}
