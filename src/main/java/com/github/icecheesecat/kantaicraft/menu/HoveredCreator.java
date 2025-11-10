package com.github.icecheesecat.kantaicraft.menu;

import com.github.icecheesecat.kantaicraft.menu.pagescreen.Page;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

public interface HoveredCreator<T extends HoveringPage> {

    float HOVERING_TICKS = 60.0f;
    T createPage();

    boolean hoveredLongEnough();

    boolean hasCreated();

    void setCreated(boolean b);

    void renderHovered(GuiGraphics guiGraphics, double mouseX, double mouseY);

}
