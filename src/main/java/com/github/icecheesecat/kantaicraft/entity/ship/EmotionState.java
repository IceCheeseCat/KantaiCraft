package com.github.icecheesecat.kantaicraft.entity.ship;


/**
 * Emotion state of basic ship entity<br>
 * duration = time in ticks and -1 equals this state won't run out of time<br>
 * <br>
 * Usage: {@link net.minecraft.network.syncher.EntityDataSerializer} will sync to client
 */
public enum EmotionState {
    NORMAL(-1),
    HAPPY(200),
    SAD(200),
    ANGRY(200),
    SERIOUS(-1),
    SHOCK(100);

    private final int duration;
    EmotionState(int duration) {
        this.duration = duration;
    }

    public static EmotionState create(int i) {
        return values()[i];
    }

    public long getDuration() {
        return duration;
    }

    public boolean isConsistent() {
        return this.duration == -1;
    }

}