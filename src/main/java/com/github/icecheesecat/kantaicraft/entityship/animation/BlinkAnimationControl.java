package com.github.icecheesecat.kantaicraft.entityship.animation;

import net.minecraft.util.RandomSource;

public class BlinkAnimationControl implements AnimationControl {

    private long nextBlinkTime;
    private final int minNextBlinkTime;
    private final int maxNextBlinkTime;
    private final RandomSource randomSource;

    public BlinkAnimationControl(int minTick, int maxTick, RandomSource randomSource) {
        this.minNextBlinkTime = minTick;
        this.maxNextBlinkTime = maxTick;
        this.randomSource = randomSource;
        this.nextBlinkTime = -1; // initialize
    }

    public boolean canAnimate(long tickCount) {
        if (tickCount >= nextBlinkTime || nextBlinkTime == -1) {
            this.nextBlinkTime = tickCount + this.randomSource.nextInt(minNextBlinkTime, maxNextBlinkTime);

            return true;
        }
        return false;
    }

}
