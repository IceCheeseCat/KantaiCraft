package com.github.icecheesecat.shincolle.weaponary.canon;

public class Canon {
    private final String name;
    private final CanonAttr canonAttr;
    private final int maxCooldown;
    private final float canon_vel;

    public Canon(String name, CanonAttr canonAttr, int cooldown, float canon_vel) {
        this.name = name;
        this.canonAttr = canonAttr;
        this.maxCooldown = cooldown;
        this.canon_vel = canon_vel;
    }

    public float getCanon_vel() {
        return canon_vel;
    }

    public CanonAttr getCanonAttr() {
        return canonAttr;
    }


    public int getMaxCooldown() {
        return maxCooldown;
    }

    public String getName() {
        return name;
    }

    public static final class Builder {
        private String name;
        private CanonAttr canonAttr;
        private int maxCooldown;
        private float canon_vel;

        public Builder() {
        }

        public static Builder aCanon() {
            return new Builder();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCanonAttr(CanonAttr canonAttr) {
            this.canonAttr = canonAttr;
            return this;
        }

        public Builder withMaxCooldown(int maxCooldown) {
            this.maxCooldown = maxCooldown;
            return this;
        }

        public Builder withCanon_vel(float canon_vel) {
            this.canon_vel = canon_vel;
            return this;
        }

        public Canon build() {
            Canon canon = new Canon(name, canonAttr, maxCooldown, canon_vel);
            return canon;
        }
    }
}
