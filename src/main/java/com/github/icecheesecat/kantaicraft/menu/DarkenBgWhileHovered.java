package com.github.icecheesecat.kantaicraft.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;

public interface DarkenBgWhileHovered {

    int DARKEN = FastColor.ARGB32.color(128, 0,0,0);


    default boolean darkenBackgroundWhenHovered() {
        return true;
    }

    default int getDarkenBackgroundColor() {
        return DARKEN;
    }

    default void renderDarkenBackground(boolean isHovered, GuiGraphics guiGraphics, int minX, int minY, int maxX, int maxY) {
        if (isHovered) {
            guiGraphics.fill(minX, minY, maxX, maxY, getDarkenBackgroundColor());
        }
    }

}
