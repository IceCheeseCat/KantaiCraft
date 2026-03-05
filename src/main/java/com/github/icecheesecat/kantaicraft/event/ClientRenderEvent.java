package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.ClientLevelTrajectoryCapability;
import com.github.icecheesecat.kantaicraft.client.renderer.TrajectoryRenderer;
import com.github.icecheesecat.kantaicraft.model.EntityShipRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)

public class ClientRenderEvent {

    @SubscribeEvent
    public static void renderDebugEntityShipBrain(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        if (Minecraft.getInstance().level != null) {
            if (!EntityShipRenderer.debug) return;
            var debugRenderer = Minecraft.getInstance().debugRenderer;
            debugRenderer.brainDebugRenderer.render(event.getPoseStack(), Minecraft.getInstance().renderBuffers().bufferSource(), event.getCamera().getPosition().x, event.getCamera().getPosition().y, event.getCamera().getPosition().z);
        }
    }

    @SubscribeEvent
    public static void renderTrajectories(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        if (Minecraft.getInstance().level != null) {
            ClientLevel level = Minecraft.getInstance().level;
            PoseStack poseStack = event.getPoseStack();
            VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());

            level.getCapability(ClientLevelTrajectoryCapability.TOKEN).ifPresent(
                    clientLevelTrajectory -> {
                        clientLevelTrajectory.getTrajectories().forEach(trajectory -> {
                            TrajectoryRenderer.renderCannonShellBoundingBox(poseStack, vertexConsumer, trajectory.getPhysics().getVel(), trajectory.getBoundingBox());
                            TrajectoryRenderer.renderArcOfTrajectory(clientLevelTrajectory.getPointsOfArc(trajectory.getId()), poseStack, vertexConsumer);
                        });
                    }
            );

        }

    }


}
