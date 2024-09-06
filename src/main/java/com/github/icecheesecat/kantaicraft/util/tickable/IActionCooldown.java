package com.github.icecheesecat.kantaicraft.util.tickable;

public interface IActionCooldown {

    void setCooldown(int c);
    int getCooldown();
    void setMaxCooldown(int c);
    int getMaxCooldown();
    void resetCooldown();
    void tickCooldown();
    boolean checkCooldown();

}
