package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.indicateditementities.IndicatedItemEntitiesCapability;
import com.github.icecheesecat.kantaicraft.capability.kantaidata.ClientPlayerKantaiDataCacheCapability;
import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.capability.trajectory.ClientLevelTrajectoryCapability;
import com.github.icecheesecat.kantaicraft.capability.trajectory.ServerLevelTrajectoryCapability;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilitiesEvent {

    @SubscribeEvent
    public static void onEntityAttachingCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (!serverPlayer.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
                    event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.player_kantai_data"), new PlayerKantaiDataCapability(serverPlayer));
                }
            }
            if (player instanceof LocalPlayer localPlayer) {
                if (!localPlayer.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
                    event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.player_kantai_data"), new PlayerKantaiDataCapability(localPlayer));
                    event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.client_player_kantai_data_cache"), new ClientPlayerKantaiDataCacheCapability());
                }
            }

        }
    }

    @SubscribeEvent
    public static void onLevelAttachingCapability(AttachCapabilitiesEvent<Level> event) {
        Level level = event.getObject();
        // server
        if (level instanceof ServerLevel serverLevel) {
            if (!level.getCapability(ServerLevelTrajectoryCapability.TOKEN).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "server_level_trajectories_capability"), new ServerLevelTrajectoryCapability(serverLevel));
            }
        }
        // client
        if (level instanceof ClientLevel clientLevel) {
            if (!level.getCapability(ClientLevelTrajectoryCapability.TOKEN).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "client_level_trajectories_capability"), new ClientLevelTrajectoryCapability(clientLevel));
            }
            if (!level.getCapability(IndicatedItemEntitiesCapability.TOKEN).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "indicated_item_entities_capability"), new IndicatedItemEntitiesCapability());
            }
        }
    }

    @SubscribeEvent
    public static void onBlockEntityAttach(AttachCapabilitiesEvent<BlockEntity> event) {
    }

    @SubscribeEvent
    public static void onItemStackAttach(AttachCapabilitiesEvent<ItemStack> event) {
    }

}
