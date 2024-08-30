package com.github.icecheesecat.shincolle.event;

import com.github.icecheesecat.shincolle.Shincolle;
import com.github.icecheesecat.shincolle.entity.destroyer.DestroyerRo.EntityDestroyerRo;
import com.github.icecheesecat.shincolle.init.ModEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shincolle.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEntityEvent {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

        event.put(ModEntity.DestroyerRo.get(), EntityDestroyerRo.createAttributes().build());

    }

}
