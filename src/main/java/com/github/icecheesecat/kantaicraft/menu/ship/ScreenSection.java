package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.menu.IconWithTextElement;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import java.util.ArrayList;
import java.util.List;

public class ScreenSection implements Renderable {

    private static final int BACKGROUND = FastColor.ARGB32.color(102, 0, 0, 0);
    private boolean isShow = true;
    private final Component title;
    protected int x, y, width, height;
    private final List<ImageDisplay> imageDisplays = new ArrayList<>();

    public ScreenSection(Component title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setShow(boolean h) {
        this.isShow = h;
    }

    public boolean isShowing() {
        return isShow;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isShow) return;
        RenderSystem.enableBlend();
        imageDisplays.forEach(imageDisplay -> {
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, imageDisplay.alpha);
            guiGraphics.blit(imageDisplay.resourceLocation, imageDisplay.x, imageDisplay.y,  imageDisplay.blitOffset, 0,  0, imageDisplay.imageWidth, imageDisplay.imageHeight, imageDisplay.textureWidth, imageDisplay.textureHeight);
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        });
        RenderSystem.disableBlend();
    }

    public Component getTitle() {
        return this.title;
    }

//    public TextGridLayout getTextGridLayout() {
//        return textGridLayout;
//    }
//
//    public void setTextGridLayout(TextGridLayout textGridLayout) {
//        this.textGridLayout = textGridLayout;
//    }

    public record ImageDisplay(int x, int y, int blitOffset, int imageWidth, int imageHeight, int textureWidth, int textureHeight, ResourceLocation resourceLocation, float alpha) {
        public ImageDisplay(int x, int y, int imageWidth, int imageHeight, ResourceLocation resourceLocation) {
            this(x, y, 0, imageWidth, imageHeight, 256, 256, resourceLocation, 1.0f);
        }
        public ImageDisplay(int x, int y, int imageWidth, int imageHeight, ResourceLocation resourceLocation, float alpha) {
            this(x, y, 0, imageWidth, imageHeight, 256, 256, resourceLocation, alpha);
        }
        public ImageDisplay(int x, int y, int blitOffset, int imageWidth, int imageHeight, ResourceLocation resourceLocation, float alpha) {
            this(x, y, blitOffset, imageWidth, imageHeight, 256, 256, resourceLocation, alpha);
        }
    }

    public void addImageDisplay(ImageDisplay imageDisplay) {
        this.imageDisplays.add(imageDisplay);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScreenSection screenSection) {
            return screenSection.getTitle().getString().equals(this.title.getString());
        }

        return false;
    }
}

