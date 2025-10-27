package com.github.icecheesecat.kantaicraft.entityship.entity;

public enum CannonFireMode {
    ROUND_ROBIN,
    VOLLEY;

    public CannonFireMode getNext() {
        int next = (this.ordinal() + 1) % values().length;
        return values()[next];
    }
}
