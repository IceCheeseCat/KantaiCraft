package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.indicateditementities.IndicatedItemEntities;
import com.github.icecheesecat.kantaicraft.capability.indicateditementities.IndicatedItemEntitiesCapability;
import com.github.icecheesecat.kantaicraft.capability.trajectory.ClientLevelTrajectory;
import com.github.icecheesecat.kantaicraft.capability.trajectory.ClientLevelTrajectoryCapability;
import com.github.icecheesecat.kantaicraft.capability.trajectory.ServerLevelTrajectory;
import com.github.icecheesecat.kantaicraft.capability.trajectory.ServerLevelTrajectoryCapability;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LevelTickEvent {

    @SubscribeEvent
    public static void tickTrajectories(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (level.isClientSide) {
            level.getCapability(ClientLevelTrajectoryCapability.TOKEN).ifPresent(ClientLevelTrajectory::tick);
        }
        else {
            level.getCapability(ServerLevelTrajectoryCapability.TOKEN).ifPresent(ServerLevelTrajectory::tick);
        }
    }

    @SubscribeEvent
    public static void itemEntities(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) {
            event.level.getCapability(IndicatedItemEntitiesCapability.TOKEN).ifPresent(IndicatedItemEntities::doTick);
        }
    }

}
