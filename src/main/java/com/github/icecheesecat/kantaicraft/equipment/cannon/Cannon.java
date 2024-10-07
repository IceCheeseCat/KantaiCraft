package com.github.icecheesecat.kantaicraft.equipment.cannon;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentLevel;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;

public class Cannon extends Equipment {
    private final CannonStats cannonStats;
    private int maxCooldown;
    private final float cannon_vel;
    private float cannon_range;

    public Cannon(int uid, String name, CannonStats cannonStats, int cooldown, float cannon_vel, float cannonRange) {
        super(EquipmentLevel.LEVEL_ONE, uid);
        this.name = name;
        this.cannonStats = cannonStats;
        this.maxCooldown = cooldown;
        this.cannon_vel = cannon_vel;
        this.cannon_range = cannonRange;
    }

    public float getCannon_vel() {
        return cannon_vel;
    }

    public CannonStats getCannonAttr() {
        return cannonStats;
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


    public static final class CannonBuilder {
        private String name;
        private CannonStats cannonStats;
        private int maxCooldown;
        private float cannon_vel;
        private float cannon_range;
        private int uid;

        public CannonBuilder() {
        }

        public CannonBuilder(Cannon other) {
            this.name = other.name;
            this.cannonStats = other.cannonStats;
            this.maxCooldown = other.maxCooldown;
            this.cannon_vel = other.cannon_vel;
            this.cannon_range = other.cannon_range;
            this.uid = other.getUid();
        }

        public static CannonBuilder aCannon() {
            return new CannonBuilder();
        }

        public CannonBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CannonBuilder withCannonAttr(CannonStats cannonStats) {
            this.cannonStats = cannonStats;
            return this;
        }

        public CannonBuilder withMaxCooldown(int maxCooldown) {
            this.maxCooldown = maxCooldown;
            return this;
        }

        public CannonBuilder withCannon_vel(float cannon_vel) {
            this.cannon_vel = cannon_vel;
            return this;
        }

        public CannonBuilder withCannon_range(float cannon_range) {
            this.cannon_range = cannon_range;
            return this;
        }

        public CannonBuilder withUid(int uid) {
            this.uid = uid;
            return this;
        }

        public Cannon build() {
            Cannon cannon = new Cannon(uid, name, cannonStats, 0, cannon_vel, 0.0f);
            cannon.cannon_range = this.cannon_range;
            cannon.maxCooldown = this.maxCooldown;
            return cannon;
        }
    }
}
