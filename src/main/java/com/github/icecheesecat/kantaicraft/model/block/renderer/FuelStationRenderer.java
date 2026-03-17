package com.github.icecheesecat.kantaicraft.model.block.renderer;

import com.github.icecheesecat.kantaicraft.block.BlockStateProperties;
import com.github.icecheesecat.kantaicraft.block.facilities.fuelstation.FuelStationBlockEntity;
import com.github.icecheesecat.kantaicraft.model.block.model.FuelStationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FuelStationRenderer extends GeoBlockRenderer<FuelStationBlockEntity> {
    public FuelStationRenderer(BlockEntityRendererProvider.Context context) {
        super(new FuelStationModel<>(context));
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
        BlockPos offset = switch (direction) {
            default -> BlockPos.ZERO;
            case WEST -> new BlockPos(-2, 0, -1);
            case NORTH -> new BlockPos(2, 0, -2);
            case SOUTH -> new BlockPos(-1, 0, 3);
        };
        poseStack.translate(offset.getX(), offset.getY(), offset.getZ());
        switch (direction) {
            case NORTH, SOUTH -> {
                poseStack.rotateAround(Axis.of(new Vector3f(0, 1, 0)).rotation((float) (angle / 180.0f * Math.PI)), 0, 0, 0);
            }
        }

        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);

        poseStack.popPose();
//        if (animatable.getLevel() != null) {
//            int i = 0;
//            DoubleBlockCombiner.NeighborCombineResult<? extends BlockEntity> neighborcombineresult = DoubleBlockCombiner.combineWithNeigbour(ModBlock.COMMAND_CENTER_BETYPE.get(), TwoPartBlock::getCombinedBlockType, TwoPartBlock::getConnectedDirection, HorizontalDirectionalBlock.FACING, blockstate, animatable.getLevel(), animatable.getBlockPos(), (p_112202_, p_112203_) -> {
//                return false;
//            });
//            i = neighborcombineresult.apply(new BrightnessCombiner<>()).get(packedLight);
//            super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, i);
//        }
//        else {
//            super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
//        }
    }

}
