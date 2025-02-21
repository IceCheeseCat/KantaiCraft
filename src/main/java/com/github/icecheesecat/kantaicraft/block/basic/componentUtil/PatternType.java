package com.github.icecheesecat.kantaicraft.block.basic.componentUtil;

import net.minecraft.util.StringRepresentable;

public enum PatternType implements StringRepresentable {

    NONE,
    SHIPYARD;

    @Override
    public String getSerializedName() {
        return switch (this) {
            case SHIPYARD -> "shipyard";
            default -> "none";
        };
    }
}
