package com.github.icecheesecat.kantaicraft.network.packet.trajectory;

import com.github.icecheesecat.kantaicraft.capability.ClientLevelTrajectoryCapability;
import com.github.icecheesecat.kantaicraft.util.Trajectory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TrajectoryPacket {

    Trajectory trajectory;

    public TrajectoryPacket(Trajectory t) {
        this.trajectory = t;
    }

    public Trajectory get() {
        return this.trajectory;
    }

    public static TrajectoryPacket decode(FriendlyByteBuf buf) {
        return new TrajectoryPacket(Trajectory.create(buf.readNbt()));
    }

    public static void encode(TrajectoryPacket packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.trajectory.serializeNBT());
    }

    public static void clientHandle(TrajectoryPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientLevel clientLevel = Minecraft.getInstance().level;
                if (clientLevel != null) {
                    clientLevel.getCapability(ClientLevelTrajectoryCapability.TOKEN).ifPresent(
                            clientLevelTrajectory -> {
                                clientLevelTrajectory.appendTrajectory(packet.get());
                            }
                    );

                }

            });
        });
        ctx.get().setPacketHandled(true);

    }

//    @Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
//    public static class ClientTrajectoryHandler {
//
//        static int color = FastColor.ARGB32.color(255, 255, 255, 255);
//
//        @SubscribeEvent
//        public static void onTrajectoryRender(RenderLevelStageEvent event) {
//            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
//
//                PoseStack poseStack = event.getPoseStack();
//
//                pre_trajectories.forEach((id, stack) -> {
//                    Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
//                    Vec3 cameraPosition = camera.getPosition();
//                    Iterator<Trajectory> it = stack.iterator();
//                    Trajectory a = null;
//                    Trajectory b = it.next();
//                    while (it.hasNext()) {
//                        a = b;
//                        b = it.next();
//                        poseStack.pushPose();
//                        poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
//                        fill(poseStack.last().pose(), (float) a.getPos().x, (float) a.getPos().y, (float) a.getPos().z, (float) b.getPos().x, (float) b.getPos().y, (float) b.getPos().z, color);
//                        poseStack.popPose();
//                    }
//
//
//                });
//
//                // render boundingBox
//                pre_trajectories.forEach((id, stack) -> {
//                    renderAABB(poseStack, Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines()), stack.peek().getBoundingBox());
//                    System.out.println(stack.peek().getBoundingBox());
//                });
//
//            }
//        }
//
//        public static void fillTrajectoryIndicator(PoseStack poseStack, float x0, float y0, float size, int color) {
//            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
//            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.debugQuads());
//
//            vertexConsumer.vertex(poseStack.last().pose(), x0 - size, y0 - .5f, 0).color(color).endVertex();
//            vertexConsumer.vertex(poseStack.last().pose(), x0 - size, y0 + .5f, 0).color(color).endVertex();
//            vertexConsumer.vertex(poseStack.last().pose(), x0 + size, y0 + .5f, 0).color(color).endVertex();
//            vertexConsumer.vertex(poseStack.last().pose(), x0 + size, y0 - .5f, 0).color(color).endVertex();
//
//        }
//
//        public static void fill(Matrix4f matrix4f, float x0, float y0, float z0, float x1, float y1, float z1, int color) {
//            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
//            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());
//
//            float f3 = (float) FastColor.ARGB32.alpha(color) / 255.0F;
//            float f = (float) FastColor.ARGB32.red(color) / 255.0F;
//            float f1 = (float) FastColor.ARGB32.green(color) / 255.0F;
//            float f2 = (float) FastColor.ARGB32.blue(color) / 255.0F;
//
//            vertexConsumer.vertex(matrix4f, x0, y0, z0).color(f, f1, f2, f3).normal(.0f, 1.0f, .0f).endVertex();
//            vertexConsumer.vertex(matrix4f, x1, y1, z1).color(f, f1, f2, f3).normal(.0f, 1.0f, .0f).endVertex();
//        }
//
//        @SubscribeEvent
//        public static void onLeaveWorld(PlayerEvent.PlayerLoggedOutEvent event) {
//            pre_trajectories.clear();
//        }
//
//        public static void addTrajectory(Trajectory t) {
////            trajectories.add(t);
//            Stack<Trajectory> q = new Stack<>();
//            q.push(t);
//            pre_trajectories.put(t.getId(), q);
//        }
//
//        public static void removeTrajectory(int id) {
//            pre_trajectories.remove(id);
//        }
//
//    }
}
