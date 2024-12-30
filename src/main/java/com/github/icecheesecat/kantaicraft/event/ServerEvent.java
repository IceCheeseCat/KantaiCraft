package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
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

}
