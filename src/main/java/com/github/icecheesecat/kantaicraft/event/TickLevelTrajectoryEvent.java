package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.ClientLevelTrajectory;
import com.github.icecheesecat.kantaicraft.capability.ClientLevelTrajectoryCapability;
import com.github.icecheesecat.kantaicraft.capability.ServerLevelTrajectory;
import com.github.icecheesecat.kantaicraft.capability.ServerLevelTrajectoryCapability;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TickLevelTrajectoryEvent {

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (level instanceof ClientLevel) {
//            System.out.println("client tick");
            level.getCapability(ClientLevelTrajectoryCapability.TOKEN).ifPresent(ClientLevelTrajectory::tick);
        }
        if (level instanceof ServerLevel serverLevel) {
//            System.out.println("server tick");
            level.getCapability(ServerLevelTrajectoryCapability.TOKEN).ifPresent(ServerLevelTrajectory::tick);
        }
    }


}
