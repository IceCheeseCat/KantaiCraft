package com.github.icecheesecat.kantaicraft.menu.ship;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public class SectionSelector extends AbstractWidget {

    private static final int BACKGROUND_SELECTED = FastColor.ARGB32.color(255, 210, 210, 210);
    private static final int BACKGROUND_UNSELECTED = FastColor.ARGB32.color(102, 0,0,0);
    private boolean isSelected = false;
    private ScreenSection screenSection;

    public SectionSelector(int pX, int pY, int pWidth, int pHeight, Component pMessage, ScreenSection screenSection) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.screenSection = screenSection;
    }

    public void setSelected(boolean s) {
        this.isSelected = s;
        this.screenSection.setShow(s);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.isSelected) {
            pGuiGraphics.fill(this.getX(), this.getY(), this.getX()+this.getWidth(), this.getY()+this.getHeight(), BACKGROUND_SELECTED);
        }
        else {
            pGuiGraphics.fill(this.getX(), this.getY(), this.getX()+this.getWidth(), this.getY()+this.getHeight(), BACKGROUND_UNSELECTED);
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        this.setSelected(true);
        this.screenSection.setShow(true);
    }
}
