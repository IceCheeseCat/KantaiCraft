package com.github.icecheesecat.kantaicraft.entity.destroyer.DestroyerRo;

import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.entity.destroyer.BasicDestroyerShip;
import com.github.icecheesecat.kantaicraft.stats.ShipStats;
import com.github.icecheesecat.kantaicraft.util.ShipFields;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.github.icecheesecat.kantaicraft.util.goal.ShipCannonAttackGoal;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * model state:
 *   0:head
 */
public class EntityDestroyerRo extends BasicDestroyerShip
{

	public EntityDestroyerRo(EntityType<? extends PathfinderMob> entityType, Level level)
	{
		super(entityType, level, new EquipmentSlots(4, List.of(EquipmentType.CANNON, EquipmentType.CANNON, EquipmentType.CANNON, EquipmentType.CANNON)));

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

}