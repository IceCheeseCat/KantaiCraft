package com.github.icecheesecat.kantaicraft.model.equipment.renderer;

import com.github.icecheesecat.kantaicraft.equipment.Equipment;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class EquipmentRenderer implements GeoRenderer<Equipment> {

    Equipment equipment;
    GeoModel<Equipment> model;

    public EquipmentRenderer(GeoModel<Equipment> model) {
        this.model = model;
    }

    @Override
    public GeoModel<Equipment> getGeoModel() {
        return model;
    }

    @Override
    public Equipment getAnimatable() {
        return equipment;
    }

    @Override
    public void fireCompileRenderLayersEvent() {

    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float v, int i) {
        return true;
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float v, int i) {

    }

    @Override
    public void updateAnimatedTextureFrame(Equipment equipment) {

    }

    @Override
    public void defaultRender(PoseStack poseStack, Equipment animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        this.equipment = animatable;
        GeoRenderer.super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
    }
}
