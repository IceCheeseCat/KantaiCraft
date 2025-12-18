package com.github.icecheesecat.kantaicraft.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class SimpleWidget extends AbstractWidget {
    public SimpleWidget(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    protected abstract void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick);


}
