package com.github.icecheesecat.kantaicraft.menu;

import com.github.icecheesecat.kantaicraft.menu.ship.Updatable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import javax.swing.*;
import java.util.function.Consumer;

public class IconWithTextElement implements LayoutElement, Renderable, Updatable {

    public int x, y;
    public ResourceLocation icon;
    public int iconWidth = 16;
    public int iconHeight = 16;
    public int width, height;
    public int padding;
    public String text;
    public int textColor;
    private static final int DEFAULT_FONT_PIXELS = 8;

    public IconWithTextElement(int x, int y, ResourceLocation icon, int padding, String text) {
        this(x, y, icon, 16, 16, padding, text, FastColor.ARGB32.color(255, 255, 255, 255));
    }

    public IconWithTextElement(int x, int y, ResourceLocation icon, int padding, String text, int textColor) {
        this(x, y, icon, 16, 16, padding, text, textColor);
    }

    public IconWithTextElement(int x, int y, ResourceLocation icon, int iconWidth, int iconHeight, int padding, String text, int textColor) {
        this.x = x;
        this.y = y;
        this.icon = icon;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.padding = padding;
        this.text = text;
        this.textColor = textColor;
        this.width = 16 + text.length() * DEFAULT_FONT_PIXELS;
        this.height = 16;
    }

    @Override
    public void setX(int pX) {
        this.x = pX;
    }

    @Override
    public void setY(int pY) {
        this.y = pY;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> pConsumer) {
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.blit(this.icon, this.x, this.y, 0, 0, 0, this.iconWidth, this.iconHeight, this.iconWidth, this.iconHeight);
        pGuiGraphics.drawString(Minecraft.getInstance().font, text, this.getX() + this.iconWidth + this.padding, this.getY() + 4, this.textColor);
    }
}
