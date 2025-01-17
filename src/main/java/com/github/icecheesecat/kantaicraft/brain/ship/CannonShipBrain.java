package com.github.icecheesecat.kantaicraft.brain.ship;

import com.github.icecheesecat.kantaicraft.brain.ship.behavior.CannonAttack;
import com.github.icecheesecat.kantaicraft.brain.ship.behavior.SoutBurnOut;
import com.github.icecheesecat.kantaicraft.brain.ship.behavior.TickAndUpdateEquipmentActionHandler;
import com.github.icecheesecat.kantaicraft.registries.ModActitvity;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import com.github.icecheesecat.kantaicraft.brain.ship.behavior.BurnFuel;
import com.github.icecheesecat.kantaicraft.registries.ModSensor;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicCannonShip;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Optional;

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
                MemoryModuleType.ATTACK_COOLING_DOWN);
    }

    public static Brain<BasicCannonShip> makeBrain(BasicCannonShip destroyerShip, Dynamic<?> dyn) {

        Brain.Provider<BasicCannonShip> brainProvider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<BasicCannonShip> brain = brainProvider.makeBrain(dyn);
        initCoreActivity(destroyerShip, brain);
        initGuardActivity(destroyerShip, brain);
        initBurnOutActivity(destroyerShip, brain);
        initIdleActivity(destroyerShip, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.CORE);
        brain.useDefaultActivity();

        return brain;
    }

    private static void initCoreActivity(BasicCannonShip destroyerShip, Brain<BasicCannonShip> brain) {
        brain.addActivity(Activity.CORE, 0,
                ImmutableList.of(
                    new BurnFuel(),
                    new TickAndUpdateEquipmentActionHandler(),
                    new LookAtTargetSink(45, 90),
                    new MoveToTargetSink()
                ));
    }

    private static void initIdleActivity(BasicCannonShip destroyerShip, Brain<BasicCannonShip> brain) {
        brain.addActivity(Activity.IDLE, 10,
                ImmutableList.of(
                        SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)),
                        new RunOne<>(ImmutableList.of(
                            Pair.of(RandomStroll.stroll(0.4F), 2),
                            Pair.of(SetWalkTargetFromLookTarget.create(0.4F, 3), 2),
                            Pair.of(new DoNothing(30, 60), 1)))
                ));
    }

    private static void initBurnOutActivity(BasicCannonShip destroyerShip, Brain<BasicCannonShip> brain) {
        brain.addActivityWithConditions(ModActitvity.BURN_OUT_FUELS.get(),
                ImmutableList.of(
                        Pair.of(0, new SoutBurnOut())
                ),
                ImmutableSet.of(Pair.of(ModMemoryModuleType.OUT_OF_FUEL.get(), MemoryStatus.VALUE_PRESENT))
        );
    }

    private static void initGuardActivity(BasicCannonShip destroyerShip, Brain<BasicCannonShip> brain) {
        brain.addActivityWithConditions(ModActitvity.GUARD.get(),
                ImmutableList.of(
                        Pair.of(0, StartAttacking.create(CannonShipBrain::findNearestValidAttackTarget)),
                        Pair.of(1, new CannonAttack()),
                        Pair.of(2, BehaviorBuilder.triggerIf(CannonShipBrain::shipCanMelee, MeleeAttack.create(40))),
                        Pair.of(2, SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F)),
                        Pair.of(5, StopAttackingIfTargetInvalid.create())),
                ImmutableSet.of(Pair.of(ModMemoryModuleType.IS_GUARDING.get(), MemoryStatus.VALUE_PRESENT))
        );
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(BasicCannonShip destroyerShip) {

        Optional<List<LivingEntity>> l = destroyerShip.getBrain().getMemory(ModMemoryModuleType.NEARBY_MONSTERS.get());
        if (l.isPresent()) {
            return Optional.of(l.get().get(0));
        }

        Optional<List<LivingEntity>> ships = destroyerShip.getBrain().getMemory(ModMemoryModuleType.NEARBY_DIFFERENT_FACTION_SHIPS.get());
        if (ships.isPresent()) {
            return Optional.of(ships.get().get(0));
        }

        return Optional.empty();

    }

    private static boolean shipCanMelee(BasicCannonShip ship) {
        return !ship.hasEnoughAmmo() && ship.canMelee();
    }


}
