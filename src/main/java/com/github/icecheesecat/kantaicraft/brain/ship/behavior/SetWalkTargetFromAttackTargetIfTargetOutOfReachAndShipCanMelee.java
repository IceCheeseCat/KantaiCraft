package com.github.icecheesecat.kantaicraft.brain.ship.behavior;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SetWalkTargetFromAttackTargetIfTargetOutOfReachAndShipCanMelee {

    private static final int PROJECTILE_ATTACK_RANGE_BUFFER = 1;

    private static BehaviorControl<Mob> create(float pSpeedModifier) {
        return create((p_147908_) -> {
            return pSpeedModifier;
        });
    }

    private static BehaviorControl<Mob> create(Function<LivingEntity, Float> pSpeedModifier) {
        return BehaviorBuilder.create((instance) -> {
            return instance.group(instance.registered(MemoryModuleType.WALK_TARGET), instance.registered(MemoryModuleType.LOOK_TARGET), instance.present(MemoryModuleType.ATTACK_TARGET), instance.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(instance, (p_258699_, p_258700_, p_258701_, p_258702_) -> {
                return (p_258694_, p_258695_, p_258696_) -> {
                    LivingEntity livingentity = instance.get(p_258701_);
                    Optional<NearestVisibleLivingEntities> optional = instance.tryGet(p_258702_);
                    if (optional.isPresent() && optional.get().contains(livingentity) && BehaviorUtils.isWithinAttackRange(p_258695_, livingentity, 1)) {
                        p_258699_.erase();
                    } else {
                        p_258700_.set(new EntityTracker(livingentity, true));
                        p_258699_.set(new WalkTarget(new EntityTracker(livingentity, false), pSpeedModifier.apply(p_258695_), 0));
                    }

                    return true;
                };
            });
        });
    }

    /**
     * create a SetWalkTargetFromAttackTargetIfTargetOutOfReach with canMelee condition
     */
    public static BehaviorControl<Mob> create(Function<LivingEntity, Float> pSpeedModifier, BasicEntityShip ship) {
        return BehaviorBuilder.create((instance) -> {
            return instance.group(instance.registered(MemoryModuleType.WALK_TARGET), instance.registered(MemoryModuleType.LOOK_TARGET), instance.present(MemoryModuleType.ATTACK_TARGET), instance.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(instance, (walkTarget, positionTracker, attackTarget, nearestVisibleLivingEntities) -> {
                return (p_258694_, p_258695_, p_258696_) -> {
                    LivingEntity livingentity = instance.get(attackTarget);
                    Optional<NearestVisibleLivingEntities> optional = instance.tryGet(nearestVisibleLivingEntities);

                    // check whether can melee
                    if (!ship.canMelee()) {
                        return false;
                    }

                    if (ship.canMelee() && ship.hasAmmo() && ship.hasAttackableEquipment()) {
                        return false;
                    }

                    if (optional.isPresent() && optional.get().contains(livingentity) && BehaviorUtils.isWithinAttackRange(p_258695_, livingentity, 1)) {
                        walkTarget.erase();
                    } else {
                        positionTracker.set(new EntityTracker(livingentity, true));
                        walkTarget.set(new WalkTarget(new EntityTracker(livingentity, false), pSpeedModifier.apply(p_258695_), 0));
                    }

                    return true;
                };
            });
        });
    }

//    private static boolean apporachMeleeIfHaveThreatAndIfNotEnoughAmmo(BasicEntityShip ship) {
//
//        double size = ship.getAttributeValue(ModAttribute.SHIPSONAL_SPACE.get());
//        List<Mob> threats = ship.level().getNearbyEntities(Mob.class, TargetingConditions.DEFAULT, ship, AABB.ofSize(ship.position(), size, size, size)).stream().filter(Mob::isAggressive).toList();
//        if (threats.size() != 0) return true;
//
//        return ship.canMelee() && !ship.hasAmmo();
//    }
}
