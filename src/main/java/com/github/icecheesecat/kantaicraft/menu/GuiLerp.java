package com.github.icecheesecat.kantaicraft.menu;

public class GuiLerp {

    public static final float EQUIPMENT_ROTATE_SCALAR = 0.01f;

    public static float linear(float a, float b, float t) {
        return b*t + a*(1.0f-t);
    }

    public static float smooth(float a, float b, float t) {
        float map = (float) (-Math.PI / 2.0f + Math.PI * t);
        float nt = (float) Math.sin(map) / 2.0f + 0.5f;

        return b * nt + a * (1.0f - nt);
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
