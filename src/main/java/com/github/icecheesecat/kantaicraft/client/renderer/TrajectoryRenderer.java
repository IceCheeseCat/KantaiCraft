package com.github.icecheesecat.kantaicraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

public class TrajectoryRenderer {

    private static final Vec3 UP = new Vec3(0, 1, 0);
    private static final Vec3 NORTH = new Vec3(1, 0, 0);
    private static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);

    public static void renderCannonShellBoundingBox(PoseStack poseStack, VertexConsumer vertexConsumer, Vec3 flyingDirection, AABB boundingBox) {
        Vec3 cameraPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        // rotate the bounding box rendering to match shell flying direction
//        Vector3f cross = UP.cross(flyingDirection).toVector3f();
//        double angle = Math.acos(UP.dot(flyingDirection) / (UP.length() * flyingDirection.length()));
//        var angleAxis = new AxisAngle4d(angle, cross.normalize());

        poseStack.pushPose();
        poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
//        poseStack.mulPose(new Quaternionf(angleAxis));

        LevelRenderer.renderLineBox(poseStack, vertexConsumer, boundingBox, 1.0f, 1.0f, 1.0f, 1.0f);
        poseStack.popPose();
    }

    private static void renderPartOfArc(PoseStack poseStack, VertexConsumer vertexConsumer, Vec3 prePos, Vec3 pos, int color) {
        Vec3 cameraPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
//        Vec3 tangent = pos.subtract(prePos);
//        Vector3f cross = UP.cross(tangent).toVector3f();
//        double angle = Math.acos(UP.dot(tangent) / (UP.length() * tangent.length()));
//        var angleAxis = new AxisAngle4d(angle, cross.normalize());

        float f3 = (float) FastColor.ARGB32.alpha(color) / 255.0F;
        float f = (float) FastColor.ARGB32.red(color) / 255.0F;
        float f1 = (float) FastColor.ARGB32.green(color) / 255.0F;
        float f2 = (float) FastColor.ARGB32.blue(color) / 255.0F;

        poseStack.pushPose();
        poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);

        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        vertexConsumer.vertex(matrix4f, (float) prePos.x, (float) prePos.y, (float) prePos.z).color(f, f1, f2, f3).normal(matrix3f, 0.0f, 1.0f, .0f).endVertex();
        vertexConsumer.vertex(matrix4f, (float) pos.x, (float) pos.y, (float) pos.z).color(f, f1, f2, f3).normal(matrix3f, 0.0f, 1.0f, .0f).endVertex();
        poseStack.popPose();
    }

    public static void renderArcOfTrajectory(List<Vec3> points, PoseStack poseStack, VertexConsumer vertexConsumer) {
        for (int i = 0; i < points.size() - 1; i++) {
            renderPartOfArc(poseStack, vertexConsumer, points.get(i), points.get(i + 1), WHITE);
        }
    }
}
