package com.github.icecheesecat.kantaicraft.model.block.renderer;

import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;

public class ShipyardRenderer implements BlockEntityRenderer<ShipyardBlockEntity> {

    BlockEntityRendererProvider.Context context;
    LivingEntity dummyEntity;

    public ShipyardRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(ShipyardBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
//        var poses = pBlockEntity.getLinkedBlockPos();
//        if (pBlockEntity.getBlockState().getValue(BlockStateProperties.PATTERN_TYPE) == PatternType.NONE) return;


//        this.drawBuiltData(pBlockEntity.getBuiltData(), pPoseStack, pPartialTick, pBuffer, packedLight);

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
