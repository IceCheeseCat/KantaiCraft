package com.github.icecheesecat.kantaicraft.client.animation.util;

import net.minecraft.world.entity.AnimationState;

public class TriggeredAnimationState extends AnimationState {

    private long triggeredTime;

    public void animateWhen(boolean pCondition, int pTickCount, float lengthInTicks) {
        if (triggeredTime != -1 && triggeredTime + lengthInTicks < pTickCount) {
            this.startIfStopped(pTickCount);
        }
        else if (pCondition && triggeredTime == -1) {
            this.startIfStopped(pTickCount);
            this.triggeredTime = pTickCount;
        } else  {
            this.stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.triggeredTime = -1;
    }

}
