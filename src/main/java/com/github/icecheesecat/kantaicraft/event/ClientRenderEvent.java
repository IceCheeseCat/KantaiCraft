package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.indicateditementities.IndicatedItemEntitiesCapability;
import com.github.icecheesecat.kantaicraft.capability.trajectory.ClientLevelTrajectoryCapability;
import com.github.icecheesecat.kantaicraft.client.renderer.IndicatedItemRenderer;
import com.github.icecheesecat.kantaicraft.client.renderer.TrajectoryRenderer;
import com.github.icecheesecat.kantaicraft.model.EntityShipRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
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
                            if (EntityShipRenderer.debug) {
                                TrajectoryRenderer.renderCannonShellBoundingBox(poseStack, vertexConsumer, trajectory.getPhysics().getVel(), trajectory.getBoundingBox());
                            }
                            TrajectoryRenderer.renderArcOfTrajectory(clientLevelTrajectory.getPointsOfArc(trajectory.getId()), poseStack, vertexConsumer);
                        });
                    }
            );

        }

    }

    @SubscribeEvent
    public static void renderIndicatedItemEntities(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) return;
        if (Minecraft.getInstance().level != null) {
            ClientLevel level = Minecraft.getInstance().level;
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

            level.getCapability(IndicatedItemEntitiesCapability.TOKEN).ifPresent(
                    indicatedItemEntities -> {
                        indicatedItemEntities.getItemEntities().forEach(id -> {
                            Entity entity = Minecraft.getInstance().level.getEntity(id);
                            if (entity instanceof ItemEntity itemEntity) {
                                IndicatedItemRenderer.render(itemEntity, poseStack, bufferSource);
                            }
                        });
                    }
            );

        }
    }


}
