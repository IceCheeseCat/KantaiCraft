package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.brain.sensor.*;
import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSensor {

    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, KantaiCraft.MODID);

    public static final RegistryObject<SensorType<NearestEnemyPlaneSensor>> NEAREST_ENEMY_PLANE_SENSOR = SENSOR_TYPES.register("sensor_type.nearest_enemy_plane", () -> new SensorType<>(NearestEnemyPlaneSensor::new));
    public static final RegistryObject<SensorType<TargetingNearbyVisibleEntitySensor>> HOSTILE_SHIP_TARGETING_SENSOR = SENSOR_TYPES.register("sensor_type.hostile_ship_targeting", () -> new SensorType<>(() -> new TargetingNearbyVisibleEntitySensor(ModSensor::hostileShipTargeting)));
    public static final RegistryObject<SensorType<TargetingNearbyVisibleEntitySensor>> PLAYER_SHIP_TARGETING_SENSOR = SENSOR_TYPES.register("sensor_type.player_ship_targeting", () -> new SensorType<>(() -> new TargetingNearbyVisibleEntitySensor(ModSensor::playerShipTargeting)));
    public static final RegistryObject<SensorType<ShipResourcesSensor>> SHIP_RESOURCES_SENSOR = SENSOR_TYPES.register("sensor_type.ship_resources_sensor", () -> new SensorType<>(ShipResourcesSensor::new));
    public static final RegistryObject<SensorType<ShipEquipmentSensor>> SHIP_EQUIPMENT_SENSOR = SENSOR_TYPES.register("sensor_type.ship_equipment_sensor", () -> new SensorType<>(ShipEquipmentSensor::new));
    public static final RegistryObject<SensorType<AttackTargetVisibilitySensor>> ATTACK_TARGET_VISIBILITY_SENSOR = SENSOR_TYPES.register("sensor_type.attack_target_visibility_sensor", () -> new SensorType<>(AttackTargetVisibilitySensor::new));
    public static final RegistryObject<SensorType<MobDropsSensor>> MOB_DROPS_SENSOR = SENSOR_TYPES.register("sensor_type.mob_drops_sensor", () -> new SensorType<>(MobDropsSensor::new));

    public static boolean playerShipTargeting(Entity entity) {
        return entity instanceof Enemy || (entity instanceof EntityShip entityShip && entityShip.isHostileSide());
    }

    public static boolean hostileShipTargeting(Entity entity) {
        return entity instanceof Enemy || entity instanceof Player || (entity instanceof EntityShip entityShip && !entityShip.isHostileSide());
    }

}
