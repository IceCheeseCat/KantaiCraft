package com.github.icecheesecat.kantaicraft.menu.ship;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public abstract class PageTitleDisplayer implements Renderable {

    int x, y;
    int width, height;
    int padding;
    int FONT_COLOR = FastColor.ARGB32.color(255, 255, 255,255);
    int SUB_FONT_COLOR = FastColor.ARGB32.color(102, 255, 255,255);
    float CURR_FONT_SCALE = 1.0f;
    float SUB_FONT_SCALE = 0.5f;

    public PageTitleDisplayer(int x, int y, int width, int height, int padding) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.padding = padding;
    }

//    public void setCurrFontScale(float CURR_FONT_SCALE) {
//        this.CURR_FONT_SCALE = CURR_FONT_SCALE;
//    }
//
//    public void setSubFontScale(float SUB_FONT_SCALE) {
//        this.SUB_FONT_SCALE = SUB_FONT_SCALE;
//    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // current
        if (getCurrentPageTitle() != null)
            pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, getCurrentPageTitle(), this.x, this.y, FONT_COLOR);
        // next
        if (getNextPageTitle() != null)
            pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, getNextPageTitle(), this.x + this.padding, this.y, SUB_FONT_COLOR);
        // previous
        if (getPrevPageTitle() != null)
            pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, getPrevPageTitle(), this.x - this.padding, this.y, SUB_FONT_COLOR);

    }

    protected abstract Component getCurrentPageTitle();
    protected abstract Component getNextPageTitle();
    protected abstract Component getPrevPageTitle();
}
