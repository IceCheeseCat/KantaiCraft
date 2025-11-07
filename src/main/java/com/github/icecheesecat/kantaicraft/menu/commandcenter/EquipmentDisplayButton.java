package com.github.icecheesecat.kantaicraft.menu.commandcenter;

import com.github.icecheesecat.kantaicraft.client.EquipmentRendererCache;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.menu.ship.CustomTextureButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.ArrayList;
import java.util.List;

public abstract class EquipmentDisplayButton extends CustomTextureButton {

    final Equipment equipment;
    final GeoRenderer<Equipment> renderer;
    List<Component> displayTexts = new ArrayList<>();

    public EquipmentDisplayButton(int pX, int pY, int pWidth, int pHeight, ResourceLocation texture, ResourceLocation hovered, Equipment equipment) {
        super(pX, pY, pWidth, pHeight, Component.empty(), texture, hovered);
        this.equipment = equipment;
        this.renderer = EquipmentRendererCache.getEquipmentRenderer(equipment.getId());
        this.setMessage(this.equipment.getName());
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderEquipmentModel(pGuiGraphics, pPartialTick);
    }

    protected void renderEquipmentModel(GuiGraphics guiGraphics, float partialTick) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(this.getX() + 15, this.getY() + 16, 10);
        renderer.defaultRender(poseStack, equipment, guiGraphics.bufferSource(), null, null, 0, partialTick, -1);
        poseStack.popPose();
    }

    protected void renderDisplayTexts(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        if (this.displayTexts == null) {
            return;
        }

        for (int i = 0; i < displayTexts.size(); i++) {
            AbstractWidget.renderScrollingString(guiGraphics, Minecraft.getInstance().font, displayTexts.get(i), this.getX(), this.getY()+2, this.getWidth(), this.getHeight(), color);
        }
    }

    @Override
    protected void renderScrollingString(GuiGraphics pGuiGraphics, Font pFont, int pWidth, int pColor) {
        int i = this.getX() + pWidth;
        int j = this.getX() + this.getWidth() - pWidth;
        renderScrollingString(pGuiGraphics, pFont, this.getMessage(), i, this.getY(), j, this.getY() + 2, pColor);
    }
}
