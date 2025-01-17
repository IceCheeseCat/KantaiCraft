package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.brain.sensor.NearbyDifferentFactionShipSensor;
import com.github.icecheesecat.kantaicraft.brain.sensor.NearbyEntityOfClassSensor;
import com.github.icecheesecat.kantaicraft.brain.sensor.NearestEnemyPlaneSensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSensor {

    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, KantaiCraft.MODID);

    public static final RegistryObject<SensorType<NearestEnemyPlaneSensor>> NEAREST_ENEMY_PLANE_SENSOR = SENSOR_TYPES.register("sensor_type.nearest_enemy_plane", () -> new SensorType<>(NearestEnemyPlaneSensor::new));
    public static final RegistryObject<SensorType<NearbyEntityOfClassSensor<Monster>>> NEARBY_MONSTER_SENSOR = SENSOR_TYPES.register("sensor_type.nearby_monster", () -> new SensorType<>(() -> new NearbyEntityOfClassSensor(Monster.class, ModMemoryModuleType.NEARBY_MONSTERS.get())));
    public static final RegistryObject<SensorType<NearbyDifferentFactionShipSensor>> NEARBY_DIFFERENT_BASIC_ENTITY_SENSOR = SENSOR_TYPES.register("sensor_type.nearby_basic_entity_ship", () -> new SensorType<>(() -> new NearbyDifferentFactionShipSensor(ModMemoryModuleType.NEARBY_DIFFERENT_FACTION_SHIPS.get())));

}
