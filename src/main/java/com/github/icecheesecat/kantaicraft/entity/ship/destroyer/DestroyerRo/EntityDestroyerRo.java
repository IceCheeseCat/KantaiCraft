package com.github.icecheesecat.kantaicraft.entity.ship.destroyer.DestroyerRo;

import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.BasicDestroyerShip;
import com.github.icecheesecat.kantaicraft.equipment.SlotChecker;
import com.github.icecheesecat.kantaicraft.util.ShipFields;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

/**
 * model state:
 *   0:head
 */
public class EntityDestroyerRo extends BasicDestroyerShip
{

	public EntityDestroyerRo(EntityType<? extends PathfinderMob> entityType, Level level)
	{
		super(entityType, level, new EquipmentSlots(4, SlotChecker.create(ImmutableSet.of(EquipmentType.CANNON, EquipmentType.RADAR))));

		this.setShipClass(ShipFields.ShipClass.DESTROYER);
		this.setShipName(ShipFields.ShipName.DestroyerRo);
		this.setCustomShipName("Destroyer Ro-Class");
		this.setCanMelee(true);
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide) return;
		this.actionHandler.tick();
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