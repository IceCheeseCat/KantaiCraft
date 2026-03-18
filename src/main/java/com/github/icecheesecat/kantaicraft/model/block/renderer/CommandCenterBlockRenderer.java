package com.github.icecheesecat.kantaicraft.model.block.renderer;

import com.github.icecheesecat.kantaicraft.block.TwoPart;
import com.github.icecheesecat.kantaicraft.block.TwoPartBlock;
import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlockEntity;
import com.github.icecheesecat.kantaicraft.model.block.BlockLight;
import com.github.icecheesecat.kantaicraft.model.block.model.CommandCenterBlockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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
            int i = BlockLight.getPackedLight(animatable.getLevel(), animatable.getBlockPos().above());
            super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, i);
        }
    }



}
