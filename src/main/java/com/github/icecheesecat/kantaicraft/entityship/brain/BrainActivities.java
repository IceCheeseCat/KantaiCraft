package com.github.icecheesecat.kantaicraft.entityship.brain;

import com.github.icecheesecat.kantaicraft.entityship.brain.behavior.*;
import com.github.icecheesecat.kantaicraft.entityship.entity.CannonEntityShip;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModActivity;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BrainActivities {

    public static Optional<? extends LivingEntity> getAttackTargetFromNearbyTargets(EntityShip entityShip) {
        Optional<List<LivingEntity>> nearbyTargets = entityShip.getBrain().getMemory(ModMemoryModuleType.NEARBY_TARGETS.get());
        return nearbyTargets.flatMap(livingEntities -> livingEntities.stream().min(
                Comparator.comparingDouble(living -> living.distanceTo(entityShip))));

    }

    public static void initCoreActivity(Brain<? extends EntityShip> brain) {
        brain.addActivity(Activity.CORE,
                ImmutableList.of(
                        Pair.of(0, new BurnFuel()),
                        Pair.of(1, new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS)),
                        Pair.of(2, new CountDownCooldownTicks(ModMemoryModuleType.PICK_UP_COOLDOWN.get())),
                        Pair.of(3, new ShipAttackTargetRemovedIfInvalid()),
                        Pair.of(4, new ReloadEquipmentActions(true)),
                        Pair.of(5, new MoveToTargetSink()),
                        Pair.of(6, new ReturnToSitDownActivity()),
                        Pair.of(7, new LookAtTargetSink(45, 90))
                ));
    }

    public static void initBurnOutActivity(EntityShip cannonEntityShip, Brain<? extends EntityShip> brain) {
        brain.addActivityWithConditions(ModActivity.BURN_OUT_FUELS.get(),
                ImmutableList.of(
                        Pair.of(0, new SoutBurnOut())
                ),
                ImmutableSet.of(Pair.of(ModMemoryModuleType.OUT_OF_FUEL.get(), MemoryStatus.VALUE_PRESENT))
        );
    }

    public static void initFightActivity(CannonEntityShip cannonShip, Brain<CannonEntityShip> brain) {
        brain.addActivityAndRemoveMemoriesWhenStopped(Activity.FIGHT, ImmutableList.of(
                Pair.of(0, CannonShipBehaviours.setLookTargetToAttackTarget(cannonShip)),
                Pair.of(1, new ShipMeleeWalkToAttackTarget(0)),
                Pair.of(2, new ShipMeleeAttack(20)),
                Pair.of(3, new ShipRangeWalkToAttackTarget(1)),
                Pair.of(4, new CannonAttackBehavior())
        ), ImmutableSet.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)), ImmutableSet.of(MemoryModuleType.ATTACK_TARGET));
    }
    
    public static class PlayerShip {
        public static void initIdleActivity(EntityShip entityShip, Brain<? extends EntityShip> brain) {
            brain.addActivityWithConditions(Activity.IDLE,
                    ImmutableList.of(
                            Pair.of(1, new OwnerHurtTarget()),
                            Pair.of(2, new GuardModeAttackTargeting()),
                            Pair.of(3, new PickUpItem()),
                            Pair.of(5, new FollowOwner(entityShip.getFollowOwnerDistance(), entityShip.getFollowTooCloseDistance())),
                            Pair.of(8, new RunOne<>(ImmutableList.of(
                                    Pair.of(BehaviorBuilder.triggerIf(EntityShip::canWonderAround,RandomStroll.stroll(entityShip.getNormalSpeedModifier())), 2),
                                    Pair.of(BehaviorBuilder.triggerIf(EntityShip::canWonderAround, SetWalkTargetFromLookTarget.create(entityShip.getNormalSpeedModifier(), 3)), 2),
                                    Pair.of(new RandomLookAround(UniformInt.of(150, 200), 30.0F, 0.0F, 15.0F), 2),
                                    Pair.of(new DoNothing(30, 60), 1)))),
                            Pair.of(10, SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)))
                    ),
                    ImmutableSet.of(Pair.of(ModMemoryModuleType.OUT_OF_FUEL.get(), MemoryStatus.VALUE_ABSENT))
            );
        }

        public static void initSittingActivity(EntityShip entityShip, Brain<? extends EntityShip> brain) {
            brain.addActivityAndRemoveMemoriesWhenStopped(ModActivity.SITTING.get(),
                    ImmutableList.of(
                            Pair.of(0, new StandUpFromSitting.WhenAttacked()),
                            Pair.of(8, new RunOne<>(ImmutableList.of(
                                    Pair.of(new RandomLookAround(UniformInt.of(150, 200), 30.0F, 0.0F, 15.0F), 2),
                                    Pair.of(new DoNothing(30, 60), 1)))),
                            Pair.of(10, SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)))
                    ),
                    ImmutableSet.of(Pair.of(ModMemoryModuleType.IS_SITTING.get(), MemoryStatus.VALUE_PRESENT)),
                    ImmutableSet.of());
        }

    }
    
    public static class HostileShip {

        public static void initIdleActivity(EntityShip entityShip, Brain<? extends EntityShip> brain) {
            brain.addActivityWithConditions(Activity.IDLE,
                    ImmutableList.of(
                            Pair.of(1, StartAttacking.create(BrainActivities::getAttackTargetFromNearbyTargets)),
                            Pair.of(10, SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60))),
                            Pair.of(10, new RunOne<>(ImmutableList.of(
                                    Pair.of(RandomStroll.stroll(0.4F), 2),
                                    Pair.of(SetWalkTargetFromLookTarget.create(0.4F, 3), 2),
                                    Pair.of(new DoNothing(30, 60), 1))))),
                    ImmutableSet.of(Pair.of(ModMemoryModuleType.OUT_OF_FUEL.get(), MemoryStatus.VALUE_ABSENT))
            );
        }
    }

}
