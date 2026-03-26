package com.github.icecheesecat.kantaicraft.menu.commandcenter;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.entityship.entity.features.ShipLeveling;
import com.github.icecheesecat.kantaicraft.model.EntityShipRenderer;
import com.github.icecheesecat.kantaicraft.util.SerializedLivingEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.util.List;

public abstract class ShipSelectButton extends AbstractButton {

    List<Component> texts;
    static int imageWidth = 110;
    static int imageHeight = 20;
    private ResourceLocation shipPageResource;
    private EntityShip entityShip;
    private EntityShipRenderer<EntityShip> entityShipRenderer;
    protected final SerializedLivingEntity serializedLivingEntity;

    public ShipSelectButton(int pX, int pY, int pWidth, int pHeight, SerializedLivingEntity serializedLivingEntity, ResourceLocation shipPageResource) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.shipPageResource = shipPageResource;
        this.serializedLivingEntity = serializedLivingEntity;
        if (serializedLivingEntity.getEntityType() != null) {
            this.entityShip = (EntityShip) serializedLivingEntity.getEntityType().create(Minecraft.getInstance().level);
            this.entityShip.setNoAnimation();
            this.entityShip.heal(10.0f);
            this.entityShipRenderer = (EntityShipRenderer<EntityShip>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(this.entityShip);
        }
        this.texts = List.of(Component.translatable(serializedLivingEntity.getEntityType().getDescriptionId()), Component.literal(ShipLeveling.create(serializedLivingEntity.getEntityTag().getCompound("shipLevel")).toString()));
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        if (!isHovered) {
            pGuiGraphics.blit(shipPageResource, this.getX(), this.getY(), 10, 0, 149, imageWidth, imageHeight, 256, 256);
        }
        else {
            pGuiGraphics.blit(shipPageResource, this.getX(), this.getY(), 10, 0, 169, imageWidth, imageHeight, 256, 256);
        }
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = getFGColor();
        this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
        this.renderEntity(pGuiGraphics);
    }

    @Override
    public void renderString(GuiGraphics pGuiGraphics, Font pFont, int pColor) {

        int heightPadding = 6;
        for (int i = 0; i < texts.size(); i++) {
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(this.getX() + 5, this.getY() + i * heightPadding, 100);
            pGuiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
            AbstractWidget.renderScrollingString(pGuiGraphics, Minecraft.getInstance().font, texts.get(i), 0, 0, this.width,  this.height, FastColor.ARGB32.color(255, 161, 188, 185));
            pGuiGraphics.pose().popPose();
        }
    }

    protected void renderEntity(GuiGraphics guiGraphics) {

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX() + 10, this.getY() + this.entityShip.getEyeHeight() * 32.0f,  50.0f);
        guiGraphics.pose().scale(20.0f, 20.f, 20.0f);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(180));
        guiGraphics.pose().mulPose(Axis.YN.rotationDegrees(180));

        var bakedModel = entityShipRenderer.getGeoModel().getBakedModel(entityShipRenderer.getGeoModel().getModelResource(entityShip));
        var headBone = bakedModel.getBone("head");
        headBone.ifPresent(bone -> {
//            System.out.println(this.entityShip.isRemoved());
            var renderType = entityShipRenderer.getRenderType(null, entityShipRenderer.getTextureLocation(null), guiGraphics.bufferSource(), 0.0f);
//            entityShipRenderer.renderCubesOfBone(guiGraphics.pose(), bone, guiGraphics.bufferSource().getBuffer(renderType), 15728880, 655360, 1.0f, 1.0f, 1.0f, 1.0f);
            bone.setTrackingMatrices(false);
            bone.setRotX(0.0f);
            bone.setRotY(0.0f);
            bone.setRotZ(0.0f);
            entityShipRenderer.renderRecursively(guiGraphics.pose(), null, bone, renderType, guiGraphics.bufferSource(), guiGraphics.bufferSource().getBuffer(renderType), true, 0.0f, 15728880, 655360, 1.0f, 1.0f, 1.0f, 1.0f);
        });

        guiGraphics.pose().popPose();

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
