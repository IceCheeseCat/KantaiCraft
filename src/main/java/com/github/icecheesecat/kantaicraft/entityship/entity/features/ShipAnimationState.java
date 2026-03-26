package com.github.icecheesecat.kantaicraft.entityship.entity.features;

public enum ShipAnimationState {
    IDLE(Type.MAIN),
    WALK(Type.TEMP),
    RUN(Type.TEMP),
    SIT(Type.TEMP);
//    GUARD(Type.MAIN);

    private final Type type;

    ShipAnimationState(Type type) {
        this.type = type;
    }

    public static ShipAnimationState create(int ordinal) {
        return values()[ordinal];
    }

    public boolean isMainState() {
        return this.type == Type.MAIN;
    }

    public boolean isTempState() {
        return this.type == Type.TEMP;
    }

    private enum Type {
        MAIN,
        TEMP
    }

}