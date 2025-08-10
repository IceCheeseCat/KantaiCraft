package com.github.icecheesecat.kantaicraft.block;

import net.minecraft.util.StringRepresentable;

public enum TwoBlockPart implements StringRepresentable {
    Front,
    Back;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
