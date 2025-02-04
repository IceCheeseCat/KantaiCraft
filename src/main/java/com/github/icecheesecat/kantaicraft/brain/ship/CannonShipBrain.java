package com.github.icecheesecat.kantaicraft.brain.ship;

import com.github.icecheesecat.kantaicraft.brain.SetWalkTargetFromAttackTargetIfTargetOutOfReachAndShipCanMelee;
import com.github.icecheesecat.kantaicraft.brain.ship.behavior.*;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModActitvity;
import com.github.icecheesecat.kantaicraft.registries.ModAttribute;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.registries.ModSensor;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicCannonShip;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class CannonShipBrain {

    private static final List<SensorType<? extends Sensor<? super BasicCannonShip>>> SENSOR_TYPES;
    private static final List<MemoryModuleType<?>> MEMORY_TYPES;

    static {
        SENSOR_TYPES = List.of(
                ModSensor.NEARBY_MONSTER_SENSOR.get(),
                ModSensor.NEARBY_DIFFERENT_BASIC_ENTITY_SENSOR.get(),
                SensorType.NEAREST_ITEMS,
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.IS_IN_WATER);
        MEMORY_TYPES = List.of(
                ModMemoryModuleType.OWNERSHIP.get(),
                ModMemoryModuleType.OUT_OF_FUEL.get(),
                ModMemoryModuleType.IS_GUARDING.get(),
                ModMemoryModuleType.NEARBY_MONSTERS.get(),
                ModMemoryModuleType.ACTION_HANDLER.get(),
                ModMemoryModuleType.NEARBY_DIFFERENT_FACTION_SHIPS.get(),
                MemoryModuleType.NEAREST_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.PATH,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                ModMemoryModuleType.KILLED_ENTITY_DROPS.get());
    }

    public static Brain<BasicCannonShip> makeBrain(BasicCannonShip basicCannonShip, Dynamic<?> dyn) {

        Brain.Provider<BasicCannonShip> brainProvider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<BasicCannonShip> brain = brainProvider.makeBrain(dyn);
        initCoreActivity(basicCannonShip, brain);
        initGuardActivity(basicCannonShip, brain);
        initBurnOutActivity(basicCannonShip, brain);
        initIdleActivity(basicCannonShip, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.CORE);
        brain.useDefaultActivity();

        return brain;
    }

    private static void initCoreActivity(BasicCannonShip basicCannonShip, Brain<BasicCannonShip> brain) {
        brain.addActivity(Activity.CORE, 0,
                ImmutableList.of(
                    new BurnFuel(),
                    new TickAndUpdateEquipmentActionHandler(),
                    new LookAtTargetSink(45, 90),
                    new MoveToTargetSink(),
                    new StayCloseToOwner(5269, 1.0d),
                    new PickUpKilledMobDrops()
                ));
    }

    private static void initIdleActivity(BasicCannonShip basicCannonShip, Brain<BasicCannonShip> brain) {
        brain.addActivity(Activity.IDLE, 10,
                ImmutableList.of(
                        SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)),
                        new RunOne<>(ImmutableList.of(
                            Pair.of(RandomStroll.stroll(0.4F), 2),
                            Pair.of(SetWalkTargetFromLookTarget.create(0.4F, 3), 2),
                            Pair.of(new DoNothing(30, 60), 1)))
                ));
    }

    private static void initBurnOutActivity(BasicCannonShip basicCannonShip, Brain<BasicCannonShip> brain) {
        brain.addActivityWithConditions(ModActitvity.BURN_OUT_FUELS.get(),
                ImmutableList.of(
                        Pair.of(0, new SoutBurnOut())
                ),
                ImmutableSet.of(Pair.of(ModMemoryModuleType.OUT_OF_FUEL.get(), MemoryStatus.VALUE_PRESENT))
        );
    }

    private static void initGuardActivity(BasicCannonShip basicCannonShip, Brain<BasicCannonShip> brain) {
        brain.addActivityWithConditions(ModActitvity.GUARD.get(),
                ImmutableList.of(
                        Pair.of(0, StartAttacking.create(CannonShipBrain::findNearestValidAttackTarget)),
                        Pair.of(1, new CannonAttack()),
                        Pair.of(2, BehaviorBuilder.triggerIf(CannonShipBrain::attackTargetIsTooClose, MeleeAttack.create(15))),
                        Pair.of(2, SetWalkTargetFromAttackTargetIfTargetOutOfReachAndShipCanMelee.create(ship -> 1.0f, basicCannonShip)),
                        Pair.of(5, StopAttackingIfTargetInvalid.create()),
                        Pair.of(10,
                            new RunOne<>(ImmutableList.of(
                                Pair.of(RandomStroll.stroll(0.4F), 2),
                                Pair.of(SetWalkTargetFromLookTarget.create(0.4F, 3), 2),
                                Pair.of(new DoNothing(30, 60), 1)))),
                        Pair.of(10, SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)))
                ),
                ImmutableSet.of(Pair.of(ModMemoryModuleType.IS_GUARDING.get(), MemoryStatus.VALUE_PRESENT))
        );
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(BasicCannonShip cannonShip) {
        Optional<List<LivingEntity>> l = cannonShip.getBrain().getMemory(ModMemoryModuleType.NEARBY_MONSTERS.get());
        if (l.isPresent()) {
            for (var le: l.get()) {
                if (cannonShip.hasLineOfSight(le)) {
                    return Optional.of(le);
                }
            }
        }

        Optional<List<LivingEntity>> ships = cannonShip.getBrain().getMemory(ModMemoryModuleType.NEARBY_DIFFERENT_FACTION_SHIPS.get());
        if (ships.isPresent()) {
            for (var le: ships.get()) {
                if (cannonShip.hasLineOfSight(le)) {
                    return Optional.of(le);
                }
            }
        }

        return Optional.empty();

    }

    private static boolean shipCanMelee(BasicCannonShip ship) {
        return !ship.hasEnoughAmmo() && ship.canMelee();
    }

    private static boolean attackTargetIsTooClose(BasicEntityShip ship) {
        if (ship.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            LivingEntity attackTarget = ship.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
            if (attackTarget.distanceTo(ship) < ship.getAttributeValue(ModAttribute.SHIPSONAL_SPACE.get())) {
                return true;
            }
        }
        return false;
    }

}
