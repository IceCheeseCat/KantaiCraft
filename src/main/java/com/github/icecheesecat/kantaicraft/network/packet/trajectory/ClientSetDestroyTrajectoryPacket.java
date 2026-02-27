package com.github.icecheesecat.kantaicraft.network.packet.trajectory;

import com.github.icecheesecat.kantaicraft.capability.ClientLevelTrajectory;
import com.github.icecheesecat.kantaicraft.capability.ClientLevelTrajectoryCapability;
import com.github.icecheesecat.kantaicraft.util.Physics;
import com.github.icecheesecat.kantaicraft.util.Trajectory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Supplier;

public class ClientSetDestroyTrajectoryPacket {

    int id;
    Vector3f lastHitLocation;
    int indexOfLast;
    Physics lastMomentPhysics;

    public ClientSetDestroyTrajectoryPacket(int id, Vector3f lastHitLocation, int indexOfLast, Physics lastMomentPhysics) {
        this.id = id;
        this.lastHitLocation = lastHitLocation;
        this.indexOfLast = indexOfLast;
        this.lastMomentPhysics = lastMomentPhysics;
    }

    public static void encode(ClientSetDestroyTrajectoryPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.id);
        buf.writeVector3f(packet.lastHitLocation);
        buf.writeInt(packet.indexOfLast);
        buf.writeNbt(packet.lastMomentPhysics.serializeNBT());
    }

    public static ClientSetDestroyTrajectoryPacket decode(FriendlyByteBuf buf) {
        return new ClientSetDestroyTrajectoryPacket(buf.readInt(), buf.readVector3f(), buf.readInt(), Physics.create(buf.readNbt()));
    }

    public static void handle(ClientSetDestroyTrajectoryPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->   {
                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;
                level.getCapability(ClientLevelTrajectoryCapability.TOKEN).ifPresent(
                        clientLevelTrajectory -> {
                            clientLevelTrajectory.getTrajectories().forEach(trajectory -> {
                                if (trajectory.getId() == packet.id) {
                                    updateTrajectoryOnClientAfterHit(trajectory, packet.lastMomentPhysics);
                                    updateTrajectoryArcOnClientAfterHit(clientLevelTrajectory, trajectory, packet.indexOfLast);
                                }
                            });

                        }
                );
            });
        });

        ctx.get().setPacketHandled(true);

    }

    private static void updateTrajectoryOnClientAfterHit(Trajectory trajectory, Physics lastMomentPhysics) {
        trajectory.setAlive(Trajectory.DESTROYER_TIME_AFTER_HIT);
        Physics oldPhysics = trajectory.getPhysics();
        trajectory.setPhysics(lastMomentPhysics);
        trajectory.setBoundingBox(trajectory.getBoundingBox().move(trajectory.getPhysics().getPos().subtract(oldPhysics.getPos())));
        trajectory.setStopped(true);
    }

    private static void updateTrajectoryArcOnClientAfterHit(ClientLevelTrajectory clientLevelTrajectory, Trajectory trajectory, int indexOfLast) {
        List<Vec3> finalArcAfterHit =  clientLevelTrajectory.trajectories_arc_poses.get(trajectory.getId()).subList(0, indexOfLast);
        clientLevelTrajectory.trajectories_arc_poses.put(trajectory.getId(), finalArcAfterHit);
    }

}
