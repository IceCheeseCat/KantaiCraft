package com.github.icecheesecat.shincolle.equipment.cannon;

import com.github.icecheesecat.shincolle.util.ShipAttrs;

public class CannonAttr extends ShipAttrs {
    private final int size; // 0 small, 1 mid, 2 big
    public CannonAttr(ShipAttrs attrs, int size) {
        super(attrs);
        this.size = size;
    }



    public int getSize() {
        return size;
    }
}
