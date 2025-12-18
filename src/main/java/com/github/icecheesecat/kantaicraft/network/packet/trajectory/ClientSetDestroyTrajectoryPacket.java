package com.github.icecheesecat.kantaicraft.network.packet.trajectory;

import com.github.icecheesecat.kantaicraft.capability.ClientLevelTrajectoryCapability;
import com.github.icecheesecat.kantaicraft.util.Trajectory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ClientSetDestroyTrajectoryPacket {

    int id;
    Vector3f lastHitLocation;
    int indexOfLast;

    public ClientSetDestroyTrajectoryPacket(int id, Vector3f lastHitLocation, int indexOfLast) {
        this.id = id;
        this.lastHitLocation = lastHitLocation;
        this.indexOfLast = indexOfLast;
    }

    public static void encode(ClientSetDestroyTrajectoryPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.id);
        buf.writeVector3f(packet.lastHitLocation);
        buf.writeInt(packet.indexOfLast);
    }

    public static ClientSetDestroyTrajectoryPacket decode(FriendlyByteBuf buf) {
        return new ClientSetDestroyTrajectoryPacket(buf.readInt(), buf.readVector3f(), buf.readInt());
    }

    public static void handle(ClientSetDestroyTrajectoryPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->   {
                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;
                level.getCapability(ClientLevelTrajectoryCapability.TOKEN).ifPresent(
                        clientServerLevelTrajectory -> {
                            clientServerLevelTrajectory.getTrajectories().forEach(trajectory -> {
                                if (trajectory.getId() == packet.id) {
                                    trajectory.setAlive(Trajectory.DESTROYER_TIME_AFTER_HIT);
//                                    trajectory.getPhysics().setNextPosition(new Vec3(packet.lastHitLocation));
                                }
                            });

                            clientServerLevelTrajectory.appendLastHitTrajectory(packet.id, packet.indexOfLast, new Vec3(packet.lastHitLocation));
                        }
                );
            });
        });

        ctx.get().setPacketHandled(true);

    }

}
