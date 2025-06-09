package com.github.icecheesecat.kantaicraft.entity;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import com.github.icecheesecat.kantaicraft.entity.attribute.shipAttributes.ShipAttributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributesEvent {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

        event.put(ModEntity.DestroyerRoClass.get(), ShipAttributes.DESTROYER_CLASS);
        event.put(ModEntity.Inazuma.get(), ShipAttributes.DESTROYER_CLASS);

    }

}
