package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.menu.pagescreen.PageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.util.FastColor;

public class PageTitleDisplayer implements Renderable {

    int x, y;
    int width, height;
    int padding;
    PageManager pageManager;
    int FONT_COLOR = FastColor.ARGB32.color(255, 255, 255,255);
    int SUB_FONT_COLOR = FastColor.ARGB32.color(102, 255, 255,255);
    float CURR_FONT_SCALE = 1.0f;
    float SUB_FONT_SCALE = 0.5f;

    public PageTitleDisplayer(PageManager pageManager, int x, int y, int width, int height, int padding) {
        this.pageManager = pageManager;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.padding = padding;
    }

    public void setCurrFontScale(float CURR_FONT_SCALE) {
        this.CURR_FONT_SCALE = CURR_FONT_SCALE;
    }

    public void setSubFontScale(float SUB_FONT_SCALE) {
        this.SUB_FONT_SCALE = SUB_FONT_SCALE;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // current
//        pGuiGraphics.pose().pushPose();
//        pGuiGraphics.pose().scale(CURR_FONT_SCALE, CURR_FONT_SCALE, CURR_FONT_SCALE);
        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, pageManager.getCurrentPageTitle(), this.x, this.y, FONT_COLOR);
//        pGuiGraphics.pose().popPose();

        // next and prev
//        pGuiGraphics.pose().pushPose();
//        pGuiGraphics.pose().scale(SUB_FONT_SCALE, SUB_FONT_SCALE, SUB_FONT_SCALE);
        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, pageManager.getNextPageTitle(), this.x + this.padding, this.y, SUB_FONT_COLOR);
        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, pageManager.getPrevPageTitle(), this.x - this.padding, this.y, SUB_FONT_COLOR);
//        pGuiGraphics.pose().popPose();

    }
}
