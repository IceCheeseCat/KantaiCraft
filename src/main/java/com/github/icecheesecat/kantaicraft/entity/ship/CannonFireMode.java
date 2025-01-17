package com.github.icecheesecat.kantaicraft.entity.ship;

public enum CannonFireMode {
    ROUND_ROBIN,
    VOLLEY;

    public CannonFireMode getNext() {
        int next = (this.ordinal() + 1) % values().length;
        return values()[next];
    }
}
