package com.github.icecheesecat.kantaicraft.model.block.renderer;

import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlockItem;
import com.github.icecheesecat.kantaicraft.model.block.model.CommandCenterBlockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CommandCenterBlockItemRenderer extends GeoItemRenderer<CommandCenterBlockItem> {
    public CommandCenterBlockItemRenderer() {
        super(new CommandCenterBlockModel<>(null));
    }

    @Override
    public void defaultRender(PoseStack poseStack, CommandCenterBlockItem animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
    }
}
