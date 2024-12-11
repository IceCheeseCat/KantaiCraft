package com.github.icecheesecat.kantaicraft.equipment.cannon;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentLevel;

public abstract class Cannon extends Equipment {
    private final int maxCooldown;
    private final float cannon_vel;
    private final float cannon_range;
    private final int size;

    public Cannon(int uid, String name, int cooldown, float cannon_vel, float cannonRange, int size) {
        super(EquipmentLevel.LEVEL_ONE, uid);
        this.size = size;
        this.name = name;
        this.maxCooldown = cooldown;
        this.cannon_vel = cannon_vel;
        this.cannon_range = cannonRange;
    }

    public float getCannon_vel() {
        return cannon_vel;
    }

    public int getMaxCooldown() {
        return maxCooldown;
    }

    public float getCannon_range() {
        return cannon_range;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

}
