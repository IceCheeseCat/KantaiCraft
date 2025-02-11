package com.github.icecheesecat.kantaicraft.config;

import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.checkerframework.checker.units.qual.A;

public class CannonStats extends EquipmentStats {

    ForgeConfigSpec.DoubleValue SIZE;
    ForgeConfigSpec.DoubleValue RANGE;
    ForgeConfigSpec.DoubleValue COOLDOWN;
    ForgeConfigSpec.DoubleValue MISSILE_VELOCITY;
    ForgeConfigSpec.DoubleValue FIRE_POWER;
    ForgeConfigSpec.DoubleValue ANTI_AIR;

    public CannonStats(ForgeConfigSpec.Builder BUILDER, String path, double size, double range, double cooldown, double missileVelocity, double firePower) {
        SIZE = BUILDER.comment("size").defineInRange(path + ".size", () -> size, 0.0d, 5.0d);
        RANGE = BUILDER.comment("range").defineInRange(path + ".range", () -> range, 0.0d, Double.MAX_VALUE);
        COOLDOWN = BUILDER.comment("cooldown").defineInRange(path + ".cooldown", () -> cooldown, 0.0d, Double.MAX_VALUE);
        MISSILE_VELOCITY = BUILDER.comment("missile velocity").defineInRange(path + ".missile_velocity", () -> missileVelocity, 0.0d, Double.MAX_VALUE);
        FIRE_POWER = BUILDER.comment("fire power").defineInRange(path + ".fire_power", () -> firePower, 0.0d, Double.MAX_VALUE);
        add(EquipmentStatType.CANNON_SIZE, SIZE);
        add(EquipmentStatType.CANNON_RANGE, RANGE);
        add(EquipmentStatType.CANNON_COOLDOWN, COOLDOWN);
        add(EquipmentStatType.CANNON_MISSLE_VELOCITY, MISSILE_VELOCITY);
        add(EquipmentStatType.FIREPOWER, FIRE_POWER);
    }

    public CannonStats(ForgeConfigSpec.Builder BUILDER, String path, double size, double range, double cooldown, double missileVelocity, double firePower, double antiAir) {
        this(BUILDER, path, size, range, cooldown, missileVelocity, firePower);
        ANTI_AIR = BUILDER.comment("anti air").defineInRange(path + ".anti_air", () -> antiAir, 0.0d, Double.MAX_VALUE);
        add(EquipmentStatType.ANTIAIR, ANTI_AIR);
    }

}
