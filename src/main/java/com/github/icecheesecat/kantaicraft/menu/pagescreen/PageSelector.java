package com.github.icecheesecat.kantaicraft.menu.pagescreen;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class PageSelector extends AbstractWidget {

//    private static final int BACKGROUND_SELECTED = FastColor.ARGB32.color(255, 210, 210, 210);
//    private static final int BACKGROUND_UNSELECTED = FastColor.ARGB32.color(102, 0,0,0);
    private static final ResourceLocation BUTTON_LOCATION = new ResourceLocation(KantaiCraft.MODID, "textures/gui/button.png");
    private static final ResourceLocation BUTTON_PRESSED_LOCATION = new ResourceLocation(KantaiCraft.MODID, "textures/gui/button_pressed.png");
    private boolean isSelected = false;
    private Page page;
    private int imageWidth;
    private int imageHeight;
    private String text;
    private int textColor = WHITE;
    private static final int BLACK = FastColor.ARGB32.color(255, 0, 0, 0);
    private static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);

    public PageSelector(int pX, int pY, int pWidth, int pHeight, Component pMessage, Page page) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.page = page;
        this.imageWidth = 64;
        this.imageHeight = 64;
    }

    public void setSelected(boolean s) {
        this.isSelected = s;
        this.page.setShow(s);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.isSelected) {
//            pGuiGraphics.fill(this.getX(), this.getY(), this.getX()+this.getWidth(), this.getY()+this.getHeight(), BACKGROUND_SELECTED);
            pGuiGraphics.blit(BUTTON_PRESSED_LOCATION, this.getX(), this.getY(), 0, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        }
        else {
            pGuiGraphics.blit(BUTTON_LOCATION, this.getX(), this.getY(), 0, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        }

        int centerX = this.getX() + this.imageWidth / 2;
        int centerY = this.getY() + 8;

        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, text, centerX, centerY, textColor);

        // draw hovered outline
        if (isHovered) {
            pGuiGraphics.hLine(this.getX(), this.getX() + this.width, this.getY(), BLACK);
            pGuiGraphics.hLine(this.getX(), this.getX() + this.width, this.getY() + this.height, BLACK);
            pGuiGraphics.vLine(this.getX(), this.getY(), this.getY() + this.height, BLACK);
            pGuiGraphics.vLine(this.getX() + this.width, this.getY(), this.getY() + this.height, BLACK);
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        this.setSelected(true);
        this.page.setShow(true);
    }

}
