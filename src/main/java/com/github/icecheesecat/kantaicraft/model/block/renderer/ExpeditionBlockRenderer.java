package com.github.icecheesecat.kantaicraft.model.block.renderer;

import com.github.icecheesecat.kantaicraft.block.TwoPart;
import com.github.icecheesecat.kantaicraft.block.TwoPartBlock;
import com.github.icecheesecat.kantaicraft.block.expedition.ExpeditionBlockEntity;
import com.github.icecheesecat.kantaicraft.model.block.BlockLight;
import com.github.icecheesecat.kantaicraft.model.block.model.ExpeditionBlockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ExpeditionBlockRenderer extends GeoBlockRenderer<ExpeditionBlockEntity> {

    public ExpeditionBlockRenderer(BlockEntityRendererProvider.Context p_context) {
        super(new ExpeditionBlockModel<>());
    }

    @Override
    public boolean shouldRenderOffScreen(ExpeditionBlockEntity p_blockEntity) {
        return true;
    }

    @Override
    public void defaultRender(PoseStack p_poseStack, ExpeditionBlockEntity p_animatable,
                              MultiBufferSource p_bufferSource, @Nullable RenderType p_renderType,
                              @Nullable VertexConsumer p_buffer, float p_yaw, float p_partialTick,
                              int p_packedLight) {
        if (p_animatable.getBlockState().getValue(TwoPartBlock.TWO_PART) == TwoPart.Back
                || p_animatable.getLevel() == null) {
            return;
        }

        int packedLight = BlockLight.getPackedLight(p_animatable.getLevel(), p_animatable.getBlockPos().above());

        super.defaultRender(p_poseStack, p_animatable, p_bufferSource, p_renderType, p_buffer,
                p_yaw, p_partialTick, packedLight);
    }
}
