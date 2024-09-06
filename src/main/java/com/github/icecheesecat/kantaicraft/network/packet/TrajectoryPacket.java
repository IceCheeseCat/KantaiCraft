package com.github.icecheesecat.kantaicraft.network.packet;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.util.Trajectory;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Matrix4f;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Supplier;

public class TrajectoryPacket {

    public Vec3 pos, vel, acc;
    public int id;

    private static final int ALLOCATE_SIZE = Double.SIZE * 3 * 3;

    public TrajectoryPacket(Trajectory t) {
        this.pos = t.getPos();
        this.vel = t.getVel();
        this.acc = t.getAcc();
        this.id = t.getId();
    }

    public static TrajectoryPacket Decode(FriendlyByteBuf buf) {
        ByteBuffer buffer = ByteBuffer.wrap(buf.readByteArray());
        buffer.rewind();

        Vec3 n_pos = new Vec3(buffer.getDouble(), buffer.getDouble(), buffer.getDouble());
        Vec3 n_vel = new Vec3(buffer.getDouble(), buffer.getDouble(), buffer.getDouble());
        Vec3 n_acc = new Vec3(buffer.getDouble(), buffer.getDouble(), buffer.getDouble());
        int id = buf.readInt();
        return new TrajectoryPacket(new Trajectory(n_pos, n_vel, n_acc, id));
    }

    public static void Encode(TrajectoryPacket packet, FriendlyByteBuf buf) {
        ByteBuffer buffer = ByteBuffer.allocate(ALLOCATE_SIZE);
        buffer.putDouble(packet.pos.x);
        buffer.putDouble(packet.pos.y);
        buffer.putDouble(packet.pos.z);
        buffer.putDouble(packet.vel.x);
        buffer.putDouble(packet.vel.y);
        buffer.putDouble(packet.vel.z);
        buffer.putDouble(packet.acc.x);
        buffer.putDouble(packet.acc.y);
        buffer.putDouble(packet.acc.z);
        buffer.rewind();

        buf.writeByteArray(buffer.array());
        buf.writeInt(packet.id);
    }

    public static void serverHandle(TrajectoryPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();

        });
        ctx.get().setPacketHandled(true);
    }

    public static void clientHandle(TrajectoryPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                var stack = new Stack<Trajectory>();
                stack.add(new Trajectory(packet));
                ClientTrajectoryHandler.pre_trajectories.put(packet.id, stack);
            });
        });
        ctx.get().setPacketHandled(true);

    }

    @Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientTrajectoryHandler {

        //        public static final List<Trajectory> trajectories = new ArrayList<>();
        private static final Map<Integer, Stack<Trajectory>> pre_trajectories = new HashMap<>();

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.CLIENT) {
                if (!Minecraft.getInstance().isPaused()) {
                    Set<Integer> r_t = new HashSet<>();

                    pre_trajectories.forEach((id, stack) -> {
                        Trajectory t = stack.peek().asCopy();
                        t.tick(Trajectory.EULER_METHOD);
                        stack.add(t);
                        if (t.checkTimeout()) {
                            r_t.add(t.getId());
                        }

                        System.out.print("Client: ");
                        t.print();
                    });
                    pre_trajectories.keySet().removeIf(r_t::contains);
                }
            }
        }

        static int color = FastColor.ARGB32.color(255, 255, 255, 255);

        @SubscribeEvent
        public static void onTrajectoryRender(RenderLevelStageEvent event) {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {

                PoseStack poseStack = event.getPoseStack();
                pre_trajectories.forEach((id, stack) -> {
                    Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
                    Vec3 cameraPosition = camera.getPosition();
                    Iterator<Trajectory> it = stack.iterator();
                    Trajectory a = null;
                    Trajectory b = it.next();
                    while (it.hasNext()) {
                        a = b;
                        b = it.next();
                        poseStack.pushPose();
                        poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
                        fill(poseStack.last().pose(), (float) a.getPos().x, (float) a.getPos().y, (float) a.getPos().z, (float) b.getPos().x, (float) b.getPos().y, (float) b.getPos().z, color);
                        poseStack.popPose();
                    }

                });

            }
        }

        public static void fillTrajectoryIndicator(PoseStack poseStack, float x0, float y0, float size, int color) {
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.debugQuads());

            vertexConsumer.vertex(poseStack.last().pose(), x0 - size, y0 - .5f, 0).color(color).endVertex();
            vertexConsumer.vertex(poseStack.last().pose(), x0 - size, y0 + .5f, 0).color(color).endVertex();
            vertexConsumer.vertex(poseStack.last().pose(), x0 + size, y0 + .5f, 0).color(color).endVertex();
            vertexConsumer.vertex(poseStack.last().pose(), x0 + size, y0 - .5f, 0).color(color).endVertex();

        }

        public static void fill(Matrix4f matrix4f, float x0, float y0, float z0, float x1, float y1, float z1, int color) {
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());

            float f3 = (float) FastColor.ARGB32.alpha(color) / 255.0F;
            float f = (float) FastColor.ARGB32.red(color) / 255.0F;
            float f1 = (float) FastColor.ARGB32.green(color) / 255.0F;
            float f2 = (float) FastColor.ARGB32.blue(color) / 255.0F;

            vertexConsumer.vertex(matrix4f, x0, y0, z0).color(f, f1, f2, f3).normal(.0f, 1.0f, .0f).endVertex();
            vertexConsumer.vertex(matrix4f, x1, y1, z1).color(f, f1, f2, f3).normal(.0f, 1.0f, .0f).endVertex();
        }

        @SubscribeEvent
        public static void onLeaveWorld(PlayerEvent.PlayerLoggedOutEvent event) {
            pre_trajectories.clear();
        }

        public static void addTrajectory(Trajectory t) {
//            trajectories.add(t);
            Stack<Trajectory> q = new Stack<>();
            q.push(t);
            pre_trajectories.put(t.getId(), q);
        }

    }
}
