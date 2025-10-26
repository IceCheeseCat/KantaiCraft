package com.github.icecheesecat.kantaicraft.client.model;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShipGeoEntityRenderer<T extends EntityShip> extends GeoEntityRenderer<T> {

    protected final float scale;
    protected final Color color;
    public static final Color HOSTILE_COLOR = Color.ofRGBA(0.1f, 0.1f, 0.1f, 1.0f);
    public static final Color NORMAL_COLOR = Color.WHITE;

    public ShipGeoEntityRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model, float scale, float shadowRadius, Color color) {
        super(renderManager, model);
        this.scale = scale;
        this.shadowRadius = shadowRadius;
        this.color = color;
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return this.model.getTextureResource(animatable);
    }

    @Override
    public void render(@NotNull T entity, float entityYaw, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(this.scale, this.scale, this.scale);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    @Override
    public Color getRenderColor(T animatable, float partialTick, int packedLight) {
        return this.color;
    }
}
