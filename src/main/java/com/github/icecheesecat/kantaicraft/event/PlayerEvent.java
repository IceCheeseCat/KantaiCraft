package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.playerkantaidata.PlayerKantaiData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvent {

    @SubscribeEvent
    public static void onPlayerLoggedIn(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent p_event) {
        syncPlayerData(p_event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawned(net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent p_event) {
        syncPlayerData(p_event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(
            net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent p_event) {
        syncPlayerData(p_event.getEntity());
    }

    private static void syncPlayerData(Player p_player) {
        if (p_player.level().isClientSide) return;

        p_player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(PlayerKantaiData::updateToSelf);
    }

    @SubscribeEvent
    public static void onPlayerCloned(net.minecraftforge.event.entity.player.PlayerEvent.Clone p_event) {
        if (p_event.getEntity().level().isClientSide) return;

        var original = p_event.getOriginal();
        boolean needsRevival = !original.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent();

        // Death invalidates the original capabilities before the replacement player receives its data.
        if (needsRevival) original.reviveCaps();
        try {
            p_event.getEntity().getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(p_data ->
                    original.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(p_data::setData));
        } finally {
            if (needsRevival) original.invalidateCaps();
        }
    }
}
