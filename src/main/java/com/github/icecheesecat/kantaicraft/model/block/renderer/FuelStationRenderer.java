package com.github.icecheesecat.kantaicraft.model.block.renderer;

import com.github.icecheesecat.kantaicraft.block.BlockStateProperties;
import com.github.icecheesecat.kantaicraft.block.facilities.fuelstation.FuelStationBlockEntity;
import com.github.icecheesecat.kantaicraft.model.block.model.FuelStationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FuelStationRenderer extends GeoBlockRenderer<FuelStationBlockEntity> {
    public FuelStationRenderer(BlockEntityRendererProvider.Context context) {
        super(new FuelStationModel(context));
    }

    @Override
    public boolean shouldRenderOffScreen(FuelStationBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public void defaultRender(PoseStack poseStack, FuelStationBlockEntity animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
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
            int i = 0;

            super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
        }
        else {
            super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
        }

        poseStack.pushPose();
        renderLavaTank(poseStack, animatable, bufferSource, packedLight);

        poseStack.popPose();
        poseStack.popPose();
    }

    private void renderLavaTank(PoseStack poseStack, FuelStationBlockEntity animatable, MultiBufferSource bufferSource, int packedLight) {

        var lavaPos = new Vector3f(4.5f/16.0f, 1.0f/16.0f,0.01f);
        poseStack.translate(lavaPos.x, lavaPos.y, lavaPos.z);

        // TODO render lava
        var fluidStack = animatable.getLavaTank().getFluid();
        if (fluidStack.isEmpty()) return;

        var fluidState = fluidStack.getFluid().defaultFluidState();
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidState);
        var stillSprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(fluidTypeExtensions.getStillTexture());
        if (stillSprite == null) return;

        int tintColor = fluidTypeExtensions.getTintColor(fluidStack);

        VertexConsumer consumer = bufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluidState));
        float percentage = (float) animatable.getLavaTank().getFluidAmount() / animatable.getLavaTank().getCapacity();
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        float width = 12.5f/16.0f - 0.1f;
        float length = 1.0f - 0.1f;
        float height = 12.5f/16.0f * percentage - 0.1f;

        drawQuads(consumer, poseStack.last().pose(), normal, 0, height, 0, length, height, width, tintColor, stillSprite.getU0(), stillSprite.getV0(), stillSprite.getU1(), stillSprite.getV1(), packedLight);

        poseStack.pushPose();
        poseStack.translate(0, 0 ,0);
        drawQuads(consumer, poseStack.last().pose(), normal, 0, 0, 0, length, height, 0, tintColor, stillSprite.getU0(), stillSprite.getV0(), stillSprite.getU1(), stillSprite.getV1(), packedLight);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        poseStack.translate(-width, 0, 0);
        drawQuads(consumer, poseStack.last().pose(), normal, 0, 0, 0, width, height, 0, tintColor, stillSprite.getU0(), stillSprite.getV0(), stillSprite.getU1(), stillSprite.getV1(), packedLight);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        poseStack.translate(-length, 0, -width);
        drawQuads(consumer, poseStack.last().pose(), normal, 0, 0, 0, length, height, 0, tintColor, stillSprite.getU0(), stillSprite.getV0(), stillSprite.getU1(), stillSprite.getV1(), packedLight);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(270.0f));
        poseStack.translate(0, 0, -length);
        drawQuads(consumer, poseStack.last().pose(), normal, 0, 0, 0, width, height, 0, tintColor, stillSprite.getU0(), stillSprite.getV0(), stillSprite.getU1(), stillSprite.getV1(), packedLight);
        poseStack.popPose();

    }


    private void drawVertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, float x, float y, float z, int tintColor, float u, float v, int packedLight) {
        consumer.vertex(pose, x, y, z)
                .color(tintColor)
                .uv(u, v)
                .uv2(packedLight)
                .normal(normal,1, 0, 0)
                .endVertex();
    }

    private void drawQuads(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, float x0, float y0, float z0, float x1, float y1, float z1, int tintColor, float u0, float v0, float u1, float v1, int packedLight) {
        drawVertex(consumer, pose, normal, x0, y0, z0, tintColor, u0, v0, packedLight);
        drawVertex(consumer, pose, normal, x0, y1, z1, tintColor, u0, v1, packedLight);
        drawVertex(consumer, pose, normal, x1, y1, z1, tintColor, u1, v1, packedLight);
        drawVertex(consumer, pose, normal, x1, y0, z0, tintColor, u1, v0, packedLight);
    }
}
