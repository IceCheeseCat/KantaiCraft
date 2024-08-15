//package com.github.icecheesecat.shincolle.event;
//
//import com.github.icecheesecat.shincolle.Shincolle;
//import com.github.icecheesecat.shincolle.entity.util.attack.Trajectory;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = Shincolle.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class ServerEvent {
//    public static Vec3 pos = new Vec3(110.0f, 0.5f, 100.0f);
//
//    @SubscribeEvent
//    public static void serverTickEvent(TickEvent.ServerTickEvent event) {
//
//
//        if (event.phase == TickEvent.Phase.END) {
//            Vec3 delta = new Vec3(-2.0f, 0, 0);
//            HitResult result = Trajectory.getHitResult(pos, (a) -> true, delta, event.getServer().overworld().getLevel());
//
////            if (result instanceof BlockHitResult bhr) {
////                System.out.println(bhr.getType());
////                System.out.println(bhr.getBlockPos() + " " + event.getServer().overworld().getLevel().getBlockState(bhr.getBlockPos()));
////            }
//            if (result instanceof EntityHitResult ehr) {
//                System.out.println(ehr.getLocation() + " " + ehr.getEntity().getName());
//            }
//
//            pos = pos.add(delta);
//            if (pos.x < 90.0f) {
//                pos = new Vec3(110.0f, 0.5f, 100.0f);
//            }
//        }
//    }
//}
