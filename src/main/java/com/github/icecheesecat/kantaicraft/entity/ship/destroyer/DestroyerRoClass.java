package com.github.icecheesecat.kantaicraft.entity.ship.destroyer;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.entity.ship.DestroyerShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class DestroyerRoClass extends DestroyerShip
{

	public DestroyerRoClass(EntityType<? extends PathfinderMob> entityType, Level level)
	{
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
	protected void initEquipments(EquipmentHandler equipmentHandler) {
		equipmentHandler.setEquipment(0, Equipments.getEquipmentInstanceById(101), this);
	}


	@Override
	public int getProcessTime() {
		return 200;
	}

	@Override
	public Rarity getRarity() {
		return Rarity.COMMON;
	}

	@Override
	public boolean isHostileShip() {
		return false;
	}
}