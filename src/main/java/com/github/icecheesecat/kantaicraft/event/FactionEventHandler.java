//package com.github.icecheesecat.kantaicraft.event;
//
//import com.github.icecheesecat.kantaicraft.KantaiCraft;
//import com.github.icecheesecat.kantaicraft.faction.FactionEvent;
//import com.github.icecheesecat.kantaicraft.faction.FactionTag;
//import com.github.icecheesecat.kantaicraft.faction.LevelFactionCapability;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import java.util.List;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class FactionEventHandler {
//
//    @SubscribeEvent
//    public static void onJoinFaction(FactionEvent.Join event) {
//        FactionTag tag = event.getFactionTag();
//        Level level = event.getLevel();
//        if (level.isClientSide) return;
//
//        level.getCapability(LevelFactionCapability.FACTION).ifPresent(
//                faction -> {
//                    faction.moveEntityToFaction(tag, event.getEntity());
//                }
//        );
//
//
//    }
//
//    @SubscribeEvent
//    public static void onCreateFaction(FactionEvent.Create event) {
//        FactionTag tag = event.getFactionTag();
//        LivingEntity creator = event.getEntity();
//        Level level = event.getLevel();
//        if (level.isClientSide) return;
//
//        AtomicBoolean success = new AtomicBoolean(false);
//        level.getCapability(LevelFactionCapability.FACTION).ifPresent(
//                faction -> {
//                    success.set(faction.createFaction(creator, tag));
//                }
//        );
//
//        if (!success.get()) {
//            event.setCanceled(true);
//        }
//    }
//
//}
