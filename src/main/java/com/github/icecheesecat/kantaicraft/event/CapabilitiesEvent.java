package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.indicateditementities.IndicatedItemEntitiesCapability;
import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.capability.trajectory.ClientLevelTrajectoryCapability;
import com.github.icecheesecat.kantaicraft.capability.trajectory.ServerLevelTrajectoryCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilitiesEvent {

    @SubscribeEvent
    public static void onEntityAttachingCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.player_kantai_data"), new PlayerKantaiDataCapability(player));
            }
        }
    }

    @SubscribeEvent
    public static void onLevelAttachingCapability(AttachCapabilitiesEvent<Level> event) {
        Level level = event.getObject();
        // server
        if (!level.isClientSide) {
            if (!level.getCapability(ServerLevelTrajectoryCapability.TOKEN).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "server_level_trajectories_capability"), new ServerLevelTrajectoryCapability(level));
            }
        }
        // client
        else {
            if (!level.getCapability(ClientLevelTrajectoryCapability.TOKEN).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "client_level_trajectories_capability"), new ClientLevelTrajectoryCapability(level));
            }
            if (!level.getCapability(IndicatedItemEntitiesCapability.TOKEN).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "indicated_item_entities_capability"), new IndicatedItemEntitiesCapability());
            }
        }
    }

}
