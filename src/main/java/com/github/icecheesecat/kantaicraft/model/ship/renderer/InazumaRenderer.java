package com.github.icecheesecat.kantaicraft.model.ship.renderer;

import com.github.icecheesecat.kantaicraft.model.EntityShipRenderer;
import com.github.icecheesecat.kantaicraft.model.equipment.BodyPart;
import com.github.icecheesecat.kantaicraft.model.equipment.OffsetsMapping;
import com.github.icecheesecat.kantaicraft.model.ship.model.InazumaModel;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.Inazuma;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Map;

public class InazumaRenderer extends EntityShipRenderer<Inazuma> {
    public InazumaRenderer(EntityRendererProvider.Context renderManager, Color color) {
        super(renderManager, new InazumaModel<>(), 0.78f, 0.4f, color);
//        super(renderManager, new InazumaModel<>(), 1.0f, 0.4f, color);
    }

    @Override
    public List<GeoRenderLayer<Inazuma>> getRenderLayers() {
        return super.getRenderLayers();
    }

    @Override
    protected Map<BodyPart, Vector3d> defineBodyPartOffsetToWeapon() {
        return OffsetsMapping.DESTROYER_DIVISION_6;
    }

    @Override
    public void render(@NotNull Inazuma entity, float entityYaw, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        var rightArmWeaponAnchor = this.getGeoModel().getBone("right_arm_weapon_anchor").get();
        poseStack.pushPose();
        poseStack.scale(this.scale, this.scale, this.scale);
//        var vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.debugQuads());
        float headX = (float) rightArmWeaponAnchor.getLocalPosition().x;
        float headY = (float) rightArmWeaponAnchor.getLocalPosition().y;
        float headZ = (float) rightArmWeaponAnchor.getLocalPosition().z;
//        System.out.println("head pos:" + headX + ", " + headY + ", " + headZ);

////        var rotation = poseStack.last().pose().rotateTowards((float) rightArmWeaponAnchor.getRotationVector().x, (float) rightArmWeaponAnchor.getRotationVector().y, (float) rightArmWeaponAnchor.getRotationVector().z, 0, 1, 0);
//        var rotation = poseStack.last().pose();
////        rotation = rotation.mul(rightArmWeaponAnchor.getModelRotationMatrix());
//        var q = new Quaternionf(1, 0, 0, 0);
//        var n_q = rotation.getNormalizedRotation(q);
////        poseStack.rotateAround(n_q, 0,0, 0);
////        rotation = rotation.rotate(90, 0, 1,0 );
//        vertexConsumer.vertex(poseStack.last().pose(), headX, headY, headZ).color(255, 0, 0, 255).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(), headX + 1.0f, headY, headZ).color(255, 0, 0, 255).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(), headX + 1.0f, headY + 1.0f, headZ).color(255, 0, 0, 255).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(), headX, headY + 1.0f, headZ).color(255, 0, 0, 255).endVertex();

//        vertexConsumer.vertex(poseStack.last().pose(), headX, headY, headZ).uv(0.0f, 0.0f).uv2(Integer.MAX_VALUE).normal(poseStack.last().normal(), 0,0,1).color(0, 0, 0, 255).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(),headX + 1.0f, headY, headZ).uv(1.0f, 0.0f).uv2(Integer.MAX_VALUE).normal(poseStack.last().normal(),0,0,1).color(0, 0, 0, 255).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(),headX, headY, headZ+ 1.0f).uv(0.0f, 1.0f).uv2(Integer.MAX_VALUE).normal(poseStack.last().normal(),0,0,1).color(0, 0, 0, 255).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(),headX+ 1.0f, headY, headZ+ 1.0f).uv(1.0f, 1.0f).uv2(Integer.MAX_VALUE).normal(poseStack.last().normal(),0,0,1).color(0, 0, 0, 255).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(),headX, headY+ 1.0f, headZ).uv(0.0f, 0.0f).uv2(Integer.MAX_VALUE).normal(poseStack.last().normal(),0,0,1).color(0, 0, 0, 255).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(),headX, headY+ 1.0f, headZ+ 1.0f).uv(1.0f, 0.0f).uv2(Integer.MAX_VALUE).normal(poseStack.last().normal(),0,0,1).color(0, 0, 0, 255).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(),headX+ 1.0f, headY+ 1.0f, headZ).uv(0.0f, 1.0f).uv2(Integer.MAX_VALUE).normal(poseStack.last().normal(),0,0,1).color(0, 0, 0, 255).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(),headX+ 1.0f, headY+ 1.0f, headZ+ 1.0f).uv(1.0f, 1.0f).uv2(Integer.MAX_VALUE).normal(poseStack.last().normal(),0,0,1).color(0, 0, 0, 255).endVertex();
        poseStack.popPose();
    }
}
