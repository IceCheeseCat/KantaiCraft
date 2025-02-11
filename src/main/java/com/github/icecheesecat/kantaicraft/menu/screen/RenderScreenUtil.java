package com.github.icecheesecat.kantaicraft.menu.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.renderable.BakedModelRenderable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderScreenUtil {

    public static void renderModel(BakedModel model, PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType) {

        var renderable = BakedModelRenderable.of(model);

        renderable.render(poseStack, bufferSource, (name -> renderType), 255, 0, 0, new BakedModelRenderable.Context(ModelData.EMPTY));

    }

    public static void floatTowardsCursor(PoseStack poseStack) {

        int width =  Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        float centerX = width/2.0f - 25.0f;
        float centerY = height/2.0f - 25.0f;
        double mouseX = Minecraft.getInstance().mouseHandler.xpos() * ((double) width / Minecraft.getInstance().getWindow().getWidth());
        double mouseY = Minecraft.getInstance().mouseHandler.ypos() * ((double) height / Minecraft.getInstance().getWindow().getHeight());


        float vecX = (float) (mouseX - centerX);
        float vecY = (float) (mouseY - centerY);
        float dis = (float) Math.sqrt(vecX*vecX + vecY*vecY);
        float relX = (float) (centerX + vecX * 0.1 / Math.log(dis));
        float relY = (float) (centerY + vecY * 0.1 / Math.log(dis));

        float vecZ = 1000.0f;

        Vector3f defaultAxis = new Vector3f(0, 0, 1);
        Vector3f rotateTo = new Vector3f(vecX, vecY, vecZ);
        float angle = defaultAxis.angle(rotateTo);
        Vector3f rotationAxis = rotateTo.cross(defaultAxis);
        System.out.println(angle);

        Quaternionf q = new Quaternionf();
        q.fromAxisAngleRad(rotationAxis, angle);


        poseStack.mulPose(q);
        poseStack.translate(relX, relY, 100);


//        poseStack.mulPose(qx);
        poseStack.scale(50, 50, 50);
    }

}
