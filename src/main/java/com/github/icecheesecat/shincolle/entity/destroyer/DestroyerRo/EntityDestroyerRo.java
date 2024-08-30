package com.github.icecheesecat.shincolle.entity.destroyer.DestroyerRo;

import com.github.icecheesecat.shincolle.entity.BasicEntityShip;
import com.github.icecheesecat.shincolle.util.ShipAttrs;
import com.github.icecheesecat.shincolle.util.ShipFields;
import com.github.icecheesecat.shincolle.util.EquipmentSlots;
import com.github.icecheesecat.shincolle.util.goal.ShipCannonAttackGoal;
import com.github.icecheesecat.shincolle.equipment.EquipmentType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
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
public class EntityDestroyerRo extends BasicEntityShip
{

	public static final ShipAttrs INITATTRS = new ShipAttrs.Builder()
			.withFirePower(10.0f)
			.withTorpedo(10.0f)
			.withAntiAir(10.0f)
			.withAsw(70.0f)
			.withLos(15.0f)
			.withLuck(10.0f)
			.withHealth(24.0f)
			.withArmor(10.0f)
			.withEvasion(5.0f)
			.withSpeed(1)
			.withAirCraft(0)
			.withFuel(15)
			.withAmmo(15)
			.build();

	public EntityDestroyerRo(EntityType<? extends PathfinderMob> entityType, Level level)
	{
		super(entityType, level , new EquipmentSlots(4, List.of(EquipmentType.CANNON, EquipmentType.CANNON, EquipmentType.CANNON, EquipmentType.CANNON)));
		// Base states
		this.shipAttrs = new ShipAttrs.Builder()
				.withFirePower(10.0f)
				.withTorpedo(10.0f)
				.withAntiAir(10.0f)
				.withAsw(70.0f)
				.withLos(15.0f)
				.withLuck(10.0f)
				.withHealth(24.0f)
				.withArmor(10.0f)
				.withEvasion(5.0f)
				.withSpeed(1)
				.withAirCraft(0)
				.withFuel(15)
				.withAmmo(15)
				.build();
		this.setShipClass(ShipFields.ShipClass.DESTROYER);
		this.setShipName(ShipFields.ShipName.DestroyerRo);
		this.setCustomShipName("Destroyer Ro-Class");
		this.setCanMelee(true);

		if (level != null && !level.isClientSide) {
			this.goalSelector.removeAllGoals((g) -> true);
			this.targetSelector.removeAllGoals((g) -> true);
			this.registerGoals();
		}

	}

	// this method is fired at Mob class constructor by default
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new ShipCannonAttackGoal(this, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0d, true));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0F));
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder b = BasicEntityShip.createAtrributes();
			b.add(Attributes.MAX_HEALTH, INITATTRS.getHealth());
			b.add(Attributes.MOVEMENT_SPEED, INITATTRS.getSpeed() * 0.25f);
			b.add(Attributes.ATTACK_DAMAGE, 10.0f);
		return b;
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide) return;
		this.actionHandler.tick();
	}

	public void modifyMaxHealth(float newHealth) {

		AttributeInstance oldAttribute = this.getAttribute(Attributes.MAX_HEALTH);
		oldAttribute.setBaseValue(newHealth);

	}
}