package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.client.EquipmentRendererCache;
import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.github.icecheesecat.kantaicraft.equipment.handler.ArmedEquipment;
import com.github.icecheesecat.kantaicraft.menu.GuiLerp;
import com.github.icecheesecat.kantaicraft.menu.SimpleWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ArmedEquipmentWidget extends SimpleWidget {

    final ArmedEquipment armedEquipment;
    final GeoRenderer<Equipment> renderer;

    private static float rotate = 0.0f;

    public ArmedEquipmentWidget(int pX, int pY, int pWidth, int pHeight, ArmedEquipment armedEquipment) {
        super(pX, pY, pWidth, pHeight, Component.literal(armedEquipment.getEquippableBoneName()));
        this.armedEquipment = armedEquipment;
        this.renderer = EquipmentRendererCache.getEquipmentRenderer(armedEquipment.getEquipment().getId());
        this.setTooltip(Tooltip.create(this.armedEquipment.getEquipment().getName()));
        this.setTooltipDelay(200);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.pose().pushPose();
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderEquipmentModel(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.pose().popPose();
    }

    @Override
    protected void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
//        pGuiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), FastColor.ARGB32.color(255, 255,255,255));
        pGuiGraphics.renderOutline(this.getX(), this.getY(), this.getWidth(), this.getHeight(), FastColor.ARGB32.color(255, 0,0,0));
    }

    protected void renderEquipmentModel(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (renderer == null) return;
        PoseStack poseStack = pGuiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(this.getX() + this.getWidth() / 2.0f, this.getY() + this.getHeight() / 2.0f + 5, 100);


        poseStack.mulPose(Axis.ZP.rotation((float) Math.PI));
        poseStack.mulPose(Axis.YP.rotation((float) Math.PI/2 + rotate));
        rotate += pPartialTick * GuiLerp.EQUIPMENT_ROTATE_SCALAR;
        poseStack.scale(50, 50, 50);

        renderer.defaultRender(poseStack, this.armedEquipment.getEquipment(), pGuiGraphics.bufferSource(), null, null, 0, pPartialTick, 255);
        poseStack.popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner() {
        return new ClientTooltipPositioner() {
            @Override
            public Vector2ic positionTooltip(int pScreenWidth, int pScreenHeight, int pMouseX, int pMouseY, int pTooltipWidth, int pTooltipHeight) {
                return new Vector2i(pScreenWidth/2 - pTooltipWidth/2, pScreenHeight - 20);
            }
        };
    }
}
