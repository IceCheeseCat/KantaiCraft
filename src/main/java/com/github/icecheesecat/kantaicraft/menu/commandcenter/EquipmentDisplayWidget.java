package com.github.icecheesecat.kantaicraft.menu.commandcenter;

import com.github.icecheesecat.kantaicraft.client.EquipmentRendererCache;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.menu.DarkenBgWhileHovered;
import com.github.icecheesecat.kantaicraft.menu.HoveredCreatorWidget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoRenderer;

public abstract class EquipmentDisplayWidget extends HoveredCreatorWidget<EquipmentDetailPage> implements DarkenBgWhileHovered {

    final Equipment equipment;
    final GeoRenderer<Equipment> renderer;
    float rotX, rotY, rotZ, floatY;
    static float floatYDelta = 0.01f;
    boolean floatTurn = false;
    final ResourceLocation texture;

    public EquipmentDisplayWidget(int pX, int pY, int pWidth, int pHeight, ResourceLocation texture, Equipment equipment) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.texture = texture;
        this.equipment = equipment;
        this.renderer = EquipmentRendererCache.getEquipmentRenderer(equipment.getId());
        this.setMessage(this.equipment.getName());
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        pGuiGraphics.blit(this.texture, this.getX(), this.getY(), 0, 0, 0, this.width, this.height, 64, 64);
        this.renderDarkenBackground(isHovered, pGuiGraphics, this.getX(), this.getY(), this.getX()+this.getWidth(), this.getY()+this.getHeight());
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderEquipmentModel(pGuiGraphics, pPartialTick);

        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

    }

    protected void renderEquipmentModel(GuiGraphics guiGraphics, float partialTick) {
        if (renderer == null) return;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(this.getX() + 15, this.getY() + 20 + floatY, 100);
        if (floatY - .5f >= 0.001f) {
            floatTurn = false;
        }
        else if (floatY - (-.5f) <= 0.001f) {
            floatTurn = true;
        }
        floatY += floatTurn ? partialTick * floatYDelta : - partialTick * floatYDelta;

        poseStack.mulPose(Axis.ZP.rotation((float) Math.PI));
        rotY += 0.01f;
        poseStack.mulPose(Axis.YP.rotation((float) Math.PI + rotY));
//        poseStack.mulPose(Axis.ZP.rotation(rotZ+=0.05f));
        poseStack.scale(20, 20, 20);
        renderer.defaultRender(poseStack, equipment, guiGraphics.bufferSource(), null, null, 0, partialTick, 255);
        poseStack.popPose();
    }

}
