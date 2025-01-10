package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.path.ShipPathFinder;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvent {

    @SubscribeEvent
    public static void livingEntityTick (LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) return;

        if (event.getEntity() instanceof Zoglin zoglin) {
            zoglin.getBrain().getMemory(MemoryModuleType.ATTACK_COOLING_DOWN).ifPresentOrElse(
                    b -> {
                        if (b) System.out.println("Attack cooling down is True - " + event.getEntity().level().getGameTime());
                        else System.out.println("Attack cooling down is False - " + event.getEntity().level().getGameTime());
                    },
                    () -> {
                        System.out.println("Attack cooling down is absent - " + event.getEntity().level().getGameTime());
                    }
            );
            zoglin.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresentOrElse(
                    le -> {
                        System.out.println("Attack target exist - " + le.getName() + event.getEntity().level().getGameTime());
                    },
                    () -> {
                        System.out.println("Attack target is absent - " + event.getEntity().level().getGameTime());
                    }
            );

            System.out.println();
        }

    }

    private static boolean flag = false;
    @SubscribeEvent
    public static void playerTick(LivingEvent.LivingTickEvent event) {

        if (event.getEntity().level().isClientSide) return;
        if (event.getEntity() instanceof Player) return;
//        System.out.println(event.getEntity().getName());
//        if (event.getEntity() instanceof BasicEntityShip ship) {
//            if (ship.getNavigation().isDone()) {
//                if (flag) {
//                    ship.getNavigation().moveTo(8.0d, -61.0d, 8.0d, 1.0d);
//                }
//                else {
//                    ship.getNavigation().moveTo(17.0d, -61.0d, 17.0d, 1.0d);
//                }
//                flag = !flag;
//            }
//
//        }

//        if (event.getPhase() == EventPriority.NORMAL) {
//            ShipPathFinder pathFinder = new ShipPathFinder(new WalkNodeEvaluator(), 10);
//            BlockPos center = event.getEntity().blockPosition().above();
//            PathNavigationRegion region = new PathNavigationRegion(event.getEntity().level(), center.offset(-10, -10, -10), center.offset(10, 10, 10));
//
//            Path path = pathFinder.findPath(region, (Mob) event.getEntity(), ImmutableSet.of(new BlockPos(17, -58, 17)), 0, 0, 0);
//            if (path.getNodeCount() == 0) return;
//            //            path.advance();
//
//            Node node = path.getNextNode();
//            System.out.println(path.toString());
//            System.out.println(node.toString());
//
//            while (!node.asBlockPos().equals(path.getEndNode().asBlockPos())) {
//                path.advance();
//                node = path.getNextNode();
//                System.out.println(node.toString());
//            }
//            System.out.println("\n\n");
//        }

    }

}
