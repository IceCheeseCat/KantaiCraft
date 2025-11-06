package com.github.icecheesecat.kantaicraft.menu;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;

import java.util.List;

public abstract class ShipSelectButton extends AbstractButton {

    List<Component> texts;
    private static final ResourceLocation COMMAND_CENTER_SELECT = new ResourceLocation(KantaiCraft.MODID, "textures/gui/command_center/command_center_select.png");
    static int imageWidth = 56;
    static int imageHeight = 120;
    static int imageHoveredWidth = 60;
    static int imageHoveredHeight = 124;

    LivingEntity livingEntity;

    public ShipSelectButton(LivingEntity livingEntity, int pX, int pY, int pWidth, int pHeight, List<Component> texts) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.texts = texts;
        this.livingEntity = livingEntity;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        if (!isHovered) {
            pGuiGraphics.blit(COMMAND_CENTER_SELECT, this.getX(), this.getY(), 0, 0, imageWidth, imageHeight);
        }
        else {
            pGuiGraphics.blit(COMMAND_CENTER_SELECT, this.getX() - 2, this.getY() - 2, 56, 0, imageHoveredWidth, imageHoveredHeight);
        }
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = getFGColor();
        this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
        this.renderEntity(pGuiGraphics, livingEntity);
    }

    @Override
    public void renderString(GuiGraphics pGuiGraphics, Font pFont, int pColor) {
        int color = FastColor.ARGB32.color(255, 255, 255, 255);
        int x = this.getX() + 2;
        int y = this.getY();
        int heightPadding = 10;
        for (int i = 0; i < texts.size(); i++) {
            AbstractWidget.renderScrollingString(pGuiGraphics, Minecraft.getInstance().font, texts.get(i), x, y + i * heightPadding, x+this.width, y + i * heightPadding + 20, color);
        }
    }

    protected void renderEntity(GuiGraphics guiGraphics, LivingEntity entity) {

        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float)Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(0 * 20.0F * ((float)Math.PI / 180F));
//        var q_rot = quaternionf.mul(quaternionf1);
        entity.yBodyRot = 180;
        entity.yHeadRot = 180;
        InventoryScreen.renderEntityInInventory(guiGraphics, this.getX() + 27, this.getY() + 115, 40, quaternionf, quaternionf1, entity);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
