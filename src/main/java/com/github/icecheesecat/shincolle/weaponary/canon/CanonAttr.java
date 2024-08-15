package com.github.icecheesecat.shincolle.weaponary.canon;

import com.github.icecheesecat.shincolle.entity.util.ShipAttrs;

public class CanonAttr extends ShipAttrs {
    private final int size; // 0 small, 1 mid, 2 big
    public CanonAttr(ShipAttrs attrs, int size) {
        super(attrs);
        this.size = size;
    }



    public int getSize() {
        return size;
    }
}
