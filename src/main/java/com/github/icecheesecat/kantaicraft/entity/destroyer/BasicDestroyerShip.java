package com.github.icecheesecat.kantaicraft.entity.destroyer;

import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import com.github.icecheesecat.kantaicraft.util.goal.ShipCannonAttackGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BasicDestroyerShip extends BasicEntityShip {

    private static final AttributeSupplier DESTROYER_GROWTH = new AttributeSupplier.Builder()
            .add(ModShipAttributes.FIREPOWER.get(), 0.5d)
            .add(ModShipAttributes.TORPEDO.get(), 1.0d)
            .add(ModShipAttributes.ANTIAIR.get(), 1.0f)
            .add(ModShipAttributes.ASW.get(), 1.0d)
            .add(ModShipAttributes.LOS.get(), 0.25d)
            .add(Attributes.MAX_HEALTH, 0.2d)
            .add(ModShipAttributes.ARMOR.get(), 0.1d).build();

    protected BasicDestroyerShip(EntityType<? extends PathfinderMob> entityType, Level level, EquipmentSlots equipmentSlot) {
        super(entityType, level, equipmentSlot);

//        if (level != null && !level.isClientSide) {
//            this.goalSelector.removeAllGoals((g) -> true);
//            this.targetSelector.removeAllGoals((g) -> true);
//            this.registerGoals();
//        }
    }

    @Override
    public AttributeSupplier getGrowth() {
        return DESTROYER_GROWTH;
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_EQUIPMENT_TYPES, "1111");
    }
}
