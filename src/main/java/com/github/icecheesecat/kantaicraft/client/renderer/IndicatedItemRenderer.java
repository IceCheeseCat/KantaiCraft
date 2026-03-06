package com.github.icecheesecat.kantaicraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;

public class IndicatedItemRenderer {

    public IndicatedItemRenderer() {
    }

    public static void render(ItemEntity itemEntity, PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.pushPose();
        Vec3 cameraPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
        LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.lines()), itemEntity.getBoundingBox(), 1.0f, 0.0f, 0.0f, 1.0f);

        poseStack.popPose();
    }

}
