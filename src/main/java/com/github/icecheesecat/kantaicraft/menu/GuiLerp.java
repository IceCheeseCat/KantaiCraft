package com.github.icecheesecat.kantaicraft.menu;

public class GuiLerp {

    public static float linear(float a, float b, float t) {
        return b*t + a*(1.0f-t);
    }

    public static float fastStartSlowEnd(float a, float b, float t) {
        return b * (float) Math.sqrt(t) + a*(1.0f - (float) Math.sqrt(t));
    }

    public static float lerpAngle(float a, float b, float t) {
        float CS = (float) ((1-t)*Math.cos(a) + t*Math.cos(b));
        float SN = (float) ((1-t)*Math.sin(a) + t*Math.sin(b));
        return (float) Math.atan2(SN,CS);
    }

}
