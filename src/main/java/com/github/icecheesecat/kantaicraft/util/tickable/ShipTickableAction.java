package com.github.icecheesecat.kantaicraft.util.tickable;

public abstract class ShipTickableAction implements IActionCooldown {
    private int maxCooldown;
    private int cooldown;

    public static final ShipTickableAction NULL = new ShipTickableAction(Integer.MAX_VALUE) {
        @Override
        public void tick() {

        }
    };

    public ShipTickableAction(int max) {
        this.maxCooldown = cooldown = max;
    }

    @Override
    public void setCooldown(int c) {
        this.cooldown = c;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void setMaxCooldown(int c) {
        this.maxCooldown = c;
    }

    @Override
    public int getMaxCooldown() {
        return this.maxCooldown;
    }

    @Override
    public void resetCooldown() {
        this.cooldown = maxCooldown;
    }

    @Override
    public void tickCooldown() {
        if (this.cooldown != 0) {
            this.cooldown--;
        }
    }

    @Override
    public boolean checkCooldown() {
        return this.cooldown <= 0;
    }

    abstract public void tick();
}
