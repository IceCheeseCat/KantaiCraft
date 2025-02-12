package com.github.icecheesecat.kantaicraft.faction;

public enum FactionType {
    HOSTILE,
    NEUTRAL,
    SIDED;

    public static FactionType get(int i) {
        return values()[i];
    }
}
