package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.playerkantaidata.PlayerKantaiDataPacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvent {

    @SubscribeEvent
    public static void onPlayerLoggedIn(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide) return;
        // sync from serve to client
        event.getEntity().getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                    ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new PlayerKantaiDataPacket(event.getEntity().getId(), playerKantaiData));
                }
        );
    }

    @SubscribeEvent
    public static void onPlayerRespawned(net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity().level().isClientSide) return;
        // sync from server to client
        event.getEntity().getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                    ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new PlayerKantaiDataPacket(event.getEntity().getId(), playerKantaiData));
                }
        );
    }

    @SubscribeEvent
    public static void onPlayerCloned(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        event.getEntity().getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData -> {
            event.getOriginal().getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(playerKantaiData::setData);
        });
    }


}
