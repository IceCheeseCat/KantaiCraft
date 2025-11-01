package com.github.icecheesecat.kantaicraft.entityship.entity.destroyer;

import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.entityship.entity.DestroyerEntityShip;
import com.github.icecheesecat.kantaicraft.entityship.stance.HostileStance;
import com.github.icecheesecat.kantaicraft.entityship.stance.PlayerStance;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public abstract class DestroyerRoClass extends DestroyerEntityShip {

	public DestroyerRoClass(EntityType<? extends PathfinderMob> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public int getProcessTime() {
		return 200;
	}

	@Override
	public Rarity getRarity() {
		return Rarity.COMMON;
	}

	public static class PlayerSide extends DestroyerRoClass implements PlayerStance {
		public PlayerSide(EntityType<? extends PathfinderMob> entityType, Level level) {
			super(entityType, level);
		}
	}
	public static class HostileSide extends DestroyerRoClass implements HostileStance {
		public HostileSide(EntityType<? extends PathfinderMob> entityType, Level level) {
			super(entityType, level);
		}
	}

}