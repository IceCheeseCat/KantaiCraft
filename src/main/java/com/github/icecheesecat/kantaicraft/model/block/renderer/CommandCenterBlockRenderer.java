package com.github.icecheesecat.kantaicraft.model.block.renderer;

import com.github.icecheesecat.kantaicraft.block.TwoPart;
import com.github.icecheesecat.kantaicraft.block.TwoPartBlock;
import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlockEntity;
import com.github.icecheesecat.kantaicraft.model.block.model.CommandCenterBlockModel;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CommandCenterBlockRenderer extends GeoBlockRenderer<CommandCenterBlockEntity> {
    public CommandCenterBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new CommandCenterBlockModel<>(context));
    }

    @Override
    public boolean shouldRenderOffScreen(CommandCenterBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public void defaultRender(PoseStack poseStack, CommandCenterBlockEntity animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        if (animatable.getBlockState().getValue(TwoPartBlock.TWO_PART) == TwoPart.Back) return;
        BlockState blockstate = animatable.getBlockState();
        if (animatable.getLevel() != null) {
            int i = 0;
            DoubleBlockCombiner.NeighborCombineResult<? extends BlockEntity> neighborcombineresult = DoubleBlockCombiner.combineWithNeigbour(ModBlock.COMMAND_CENTER_BETYPE.get(), TwoPartBlock::getCombinedBlockType, TwoPartBlock::getConnectedDirection, HorizontalDirectionalBlock.FACING, blockstate, animatable.getLevel(), animatable.getBlockPos(), (p_112202_, p_112203_) -> {
                return false;
            });
            i = neighborcombineresult.apply(new BrightnessCombiner<>()).get(packedLight);
            super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, i);
        }
        else {
            super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
        }
    }

}
