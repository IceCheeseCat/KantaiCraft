package com.github.icecheesecat.kantaicraft.entity.ship.destroyer;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entity.EntityID;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicDestroyerShip;
import com.github.icecheesecat.kantaicraft.equipment.Equipments;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class EntityDestroyerRoClass extends BasicDestroyerShip
{

	public EntityDestroyerRoClass(EntityType<? extends PathfinderMob> entityType, Level level)
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
	protected void initEquipments() {
		this.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
				handler -> {
					handler.setEquipment(0, Equipments.getEquipmentInstanceById(101), this);
				}
		);
	}

	@Override
	public EntityID getEntityId() {
		return EntityID.DestroyerRoClass;
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