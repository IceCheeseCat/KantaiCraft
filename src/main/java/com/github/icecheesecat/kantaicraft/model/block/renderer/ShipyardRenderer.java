package com.github.icecheesecat.kantaicraft.model.block.renderer;

import com.github.icecheesecat.kantaicraft.block.BlockStateProperties;
import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.model.block.model.ShipyardModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ShipyardRenderer extends GeoBlockRenderer<ShipyardBlockEntity> {

    public ShipyardRenderer(BlockEntityRendererProvider.Context context) {
        super(new ShipyardModel(context));
    }

    @Override
    public void defaultRender(PoseStack poseStack, ShipyardBlockEntity animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {

        BlockState blockstate = animatable.getBlockState();
        if (!blockstate.getValue(BlockStateProperties.WORKING_FACILITY)) return;
        Direction direction = blockstate.getValue(BlockStateProperties.FACILITY_FACING);
        poseStack.pushPose();
        float angle = switch (direction) {
            default -> 0.0f;
            case SOUTH -> 90.0f;
            case WEST -> 180.0f;
            case NORTH -> 270.0f;
        };

        poseStack.rotateAround(Axis.YN.rotation((float) (angle / 180.0f * Math.PI)), 0.5f, 0, 0.5f);
        if (animatable.getLevel() != null) {
            super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
        }

        poseStack.popPose();
    }

    @Override
    public void preRender(PoseStack poseStack, ShipyardBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float percentage = (float) animatable.getProcessAt(0) / animatable.getMaxProcessAt(0);
        int renderedLayer = (int) (6 * percentage);

        if (animatable.hasProcess(0)) {
            for (int i = 0; i < 6; i++) {
                var optionalBone = model.getBone(String.valueOf(i));
                if (optionalBone.isPresent()) {
                    evaluateBone(String.valueOf(i), renderedLayer, optionalBone.get());
                }
            }
        }
        else {
            for (int i = 0; i < 6; i++) {
                var optionalBone = model.getBone(String.valueOf(i));
                optionalBone.ifPresent(this::hideBone);
            }
        }


        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    private void evaluateBone(String name, int layer, GeoBone bone) {
        if (bone.getName().equals(name)) {
            if (layer > Integer.parseInt(name)) {
                showBone(bone);
            }
            else {
                hideBone(bone);
            }
        }
    }

    private void hideBone(GeoBone bone) {
        bone.setScaleX(0.0f);
        bone.setScaleY(0.0f);
        bone.setScaleZ(0.0f);
    }

    private void showBone(GeoBone bone) {
        bone.setScaleX(1.0f);
        bone.setScaleY(1.0f);
        bone.setScaleZ(1.0f);
    }

    //    private void drawBuiltData(List<BuiltData> builtData, PoseStack poseStack, float partialTick, MultiBufferSource pBuffer, int packedLight) {
//        if (builtData == null || builtData.isEmpty()) return;
//        if (tempData == null) {
//            this.tempData = builtData.get(0);
//            this.dummyEntity = this.tempData.getData().getEntityType().create(Minecraft.getInstance().level);
//        }
//        else if (!tempData.equals(builtData.get(0))) {
//            this.tempData = builtData.get(0);
//            this.dummyEntity = this.tempData.getData().getEntityType().create(Minecraft.getInstance().level);
//        }
//
//        poseStack.pushPose();
//        poseStack.translate(0.5f, 0.5f, 0.5f);
//        poseStack.scale(0.3f, 0.3f, 0.3f);
//        EntityRenderer<LivingEntity> entityRenderer = (EntityRenderer<LivingEntity>) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(this.tempData.getData().getEntityType());
//        entityRenderer.render(dummyEntity, 0, partialTick, poseStack, pBuffer, packedLight);
//        poseStack.popPose();
//    }

}
