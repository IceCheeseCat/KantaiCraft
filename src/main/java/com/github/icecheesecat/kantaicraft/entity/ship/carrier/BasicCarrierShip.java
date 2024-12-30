package com.github.icecheesecat.kantaicraft.entity.ship.carrier;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.entity.plane.BasicEntityPlane;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public abstract class BasicCarrierShip extends BasicEntityShip {

    protected BasicCarrierShip(EntityType<? extends PathfinderMob> entityType, Level level, EquipmentSlots equipmentSlot) {
        super(entityType, level, equipmentSlot);
    }

    public BasicEntityPlane launchPlane() {

//        this.level().addFreshEntity(new Zombie());
        Equipment equipment = this.equipmentSlot.getEquipments().get(0);

        return null;
    }
}
