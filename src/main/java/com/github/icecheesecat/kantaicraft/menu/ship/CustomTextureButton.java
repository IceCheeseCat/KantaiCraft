package com.github.icecheesecat.kantaicraft.menu.ship;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public abstract class CustomTextureButton extends AbstractButton {
    ResourceLocation texture;
    ResourceLocation hoveredTexture;

    public CustomTextureButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation texture) {
        this(pX, pY, pWidth, pHeight, pMessage, texture, texture);
    }

    public CustomTextureButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation texture, ResourceLocation hoveredTexture) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.texture = texture;
        this.hoveredTexture = hoveredTexture;
        this.setX(this.getX() - this.width/2);
        this.setY(this.getY() - this.height/2);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
//        pGuiGraphics.blitNineSliced(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, 0);
        if (!isHovered) {
            pGuiGraphics.blit(texture, this.getX(), this.getY(), 10, 0, 0, this.width, this.height, this.width, this.height);
        } else {
            pGuiGraphics.blit(hoveredTexture, this.getX(), this.getY(), 10, 0, 0, this.width, this.height, this.width, this.height);
        }
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = getFGColor();
        this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
