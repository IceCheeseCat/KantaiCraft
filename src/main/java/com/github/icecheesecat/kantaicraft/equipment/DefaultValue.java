package com.github.icecheesecat.kantaicraft.equipment;

import java.util.HashMap;
import java.util.Map;

public class DefaultValue {
    protected Map<EquipmentStatType, Double> stats = new HashMap<>();

    public DefaultValue() {
    }

    public DefaultValue addStats(Map<EquipmentStatType, Double> n_stats) {
        this.stats.putAll(n_stats);
        return this;
    }

    public DefaultValue addStat(EquipmentStatType statType, double val) {
        this.stats.put(statType, val);
        return this;
    }

    public Map<EquipmentStatType, Double> getStats() {
        return stats;
    }

    private static DefaultValue createCannon(int size, double missile_velocity, double range, long cooldown) {
        return new DefaultValue().addStats(Map.of(
                EquipmentStatType.CANNON_SIZE, (double) size,
                EquipmentStatType.CANNON_MISSILE_VELOCITY, missile_velocity,
                EquipmentStatType.CANNON_RANGE, range,
                EquipmentStatType.CANNON_COOLDOWN, (double) cooldown
        ));
    }

    public static DefaultValue createSmallCannon(double missile_velocity, double range, long cooldown) {
        return createCannon(0, missile_velocity, range, cooldown);
    }

    public static DefaultValue createMediumCannon(double missile_velocity, double range, long cooldown) {
        return createCannon(1, missile_velocity, range, cooldown);
    }

    public static DefaultValue createLargeCannon(double missile_velocity, double range, long cooldown) {
        return createCannon(2, missile_velocity, range, cooldown);
    }

    public static final Map<Integer, DefaultValue> ALL_DEFAULT_VALUES = new HashMap<>();

    public static void register(int id, DefaultValue defaultValue) {
        if (ALL_DEFAULT_VALUES.containsKey(id)) {
            throw new IllegalStateException("Default value of " + id + " has already registered!");
        }
        ALL_DEFAULT_VALUES.put(id, defaultValue);
    }

    public static DefaultValue getById(int id) {
        if (!ALL_DEFAULT_VALUES.containsKey(id)) {
            throw new IllegalStateException("No default value for "+ id +" registered!");
        }

        return DefaultValue.ALL_DEFAULT_VALUES.get(id);
    }

    static {
        // small cannon
        register(EquipmentProperties.__12cm_single_gun_mount__.getId(),
                createSmallCannon(400.0d, 45.0d, 100)
                        .addStat(EquipmentStatType.FIREPOWER, 1.0d)
                        .addStat(EquipmentStatType.ANTIAIR, 1.0d));
        register(EquipmentProperties.__12cm_twin_gun_mount__.getId(),
                createSmallCannon(400.0d, 45.0d, 100)
                        .addStat(EquipmentStatType.FIREPOWER, 2.0d)
                        .addStat(EquipmentStatType.ANTIAIR, 2.0d));
        register(EquipmentProperties.__12cm_twin_gun_mount_model_b_kai_2__.getId(),
                createSmallCannon(400.0d, 45.0d, 100)
                        .addStat(EquipmentStatType.FIREPOWER, 3.0d)
                        .addStat(EquipmentStatType.ANTIAIR, 2.0d));

        // medium cannon
        register(EquipmentProperties.__14cm_single_gun_mount__.getId(),
                createMediumCannon(400.0d, 45.0d, 200)
                        .addStat(EquipmentStatType.FIREPOWER, 2.0d)
                        .addStat(EquipmentStatType.ANTIAIR, 1.0d));
        register(EquipmentProperties.__155mm_triple_gun_mount__.getId(),
                createMediumCannon(400.0d, 45.0d, 200)
                        .addStat(EquipmentStatType.FIREPOWER, 7.0d)
                        .addStat(EquipmentStatType.ANTIAIR, 4.0d)
                        .addStat(EquipmentStatType.ACCURACY, 1.0d));
        register(EquipmentProperties.__203mm_twin_gun_mount__.getId(),
                createMediumCannon(400.0d, 45.0d, 200)
                    .addStat(EquipmentStatType.FIREPOWER, 8.0d)
                    .addStat(EquipmentStatType.ANTIAIR, 3.0d));
        register(EquipmentProperties.__203mm_no3_single_gun_mount__.getId(),
                createMediumCannon(400.0d, 45.0d, 200)
                    .addStat(EquipmentStatType.FIREPOWER, 10.0d)
                    .addStat(EquipmentStatType.ANTIAIR, 4.0d)
                    .addStat(EquipmentStatType.ACCURACY, 1.0d));
        register(EquipmentProperties.__prototype_203mm_no4_single_gun_mount__.getId(),
                createMediumCannon(400.0d, 45.0d, 200)
                    .addStat(EquipmentStatType.FIREPOWER, 11.0d)
                    .addStat(EquipmentStatType.ANTIAIR, 4.0d)
                    .addStat(EquipmentStatType.ARMOR, 1.0d)
                    .addStat(EquipmentStatType.ACCURACY, 2.0d));

        // large cannon
        register(EquipmentProperties.__356mm_twin_gun_mount__.getId(),
                createLargeCannon(400.0d, 45.0d, 400)
                    .addStat(EquipmentStatType.FIREPOWER, 15.0d)
                    .addStat(EquipmentStatType.ANTIAIR, 4.0d));
        register(EquipmentProperties.__prototype_356mm_triple_gun_mount__.getId(),
                createLargeCannon(400.0d, 45.0d, 400)
                    .addStat(EquipmentStatType.FIREPOWER, 18.0d)
                    .addStat(EquipmentStatType.ANTIAIR, 5.0d)
                    .addStat(EquipmentStatType.ACCURACY, 2.0d));
        register(EquipmentProperties.__356mm_twin_gun_mount_kai_2__.getId(),
                createLargeCannon(400.0d, 45.0d, 400)
                    .addStat(EquipmentStatType.FIREPOWER, 17.0d)
                    .addStat(EquipmentStatType.ANTIAIR, 5.0d)
                    .addStat(EquipmentStatType.ARMOR, 2.0d)
                    .addStat(EquipmentStatType.ACCURACY, 5.0d)
                    .addStat(EquipmentStatType.EVASION, 2.0d));
        register(EquipmentProperties.__41cm_twin_gun_mount__.getId(),
                createLargeCannon(400.0d, 45.0d, 400)
                    .addStat(EquipmentStatType.FIREPOWER, 20.0d)
                    .addStat(EquipmentStatType.ANTIAIR, 4.0d));
        register(EquipmentProperties.__prototype_41cm_triple_gun_mount__.getId(),
                createLargeCannon(400.0d, 45.0d, 400)
                    .addStat(EquipmentStatType.FIREPOWER, 20.0d)
                    .addStat(EquipmentStatType.ANTIAIR, 4.0d));
        register(EquipmentProperties.__41cm_triple_gun_mount_kai__.getId(),
                createLargeCannon(400.0d, 45.0d, 400)
                    .addStat(EquipmentStatType.FIREPOWER, 22.0d)
                    .addStat(EquipmentStatType.ANTIAIR, 6.0d)
                    .addStat(EquipmentStatType.ARMOR, 1.0d)
                    .addStat(EquipmentStatType.ACCURACY, 4.0d));
        register(EquipmentProperties.__41cm_triple_gun_mount_kai_2__.getId(),
                createLargeCannon(400.0d, 45.0d, 400)
                    .addStat(EquipmentStatType.FIREPOWER, 23.0d)
                    .addStat(EquipmentStatType.ANTIAIR, 6.0d)
                    .addStat(EquipmentStatType.ACCURACY, 5.0d));
    }


}
