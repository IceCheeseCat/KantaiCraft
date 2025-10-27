package com.github.icecheesecat.kantaicraft.entityship;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import com.github.icecheesecat.kantaicraft.entityship.attribute.shipAttributes.ShipAttributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributesEvent {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

        event.put(ModEntity.PlayerShip.DESTROYER_RO_CLASS.get(), ShipAttributes.DESTROYER_CLASS);
        event.put(ModEntity.PlayerShip.INAZUMA.get(), ShipAttributes.DESTROYER_CLASS);
        event.put(ModEntity.HostileShip.INAZUMA.get(), ShipAttributes.DESTROYER_CLASS);
        event.put(ModEntity.PlayerShip.IKAZUCHI.get(), ShipAttributes.DESTROYER_CLASS);
        event.put(ModEntity.HostileShip.IKAZUCHI.get(), ShipAttributes.DESTROYER_CLASS);
        event.put(ModEntity.PlayerShip.HIBIKI.get(), ShipAttributes.DESTROYER_CLASS);
        event.put(ModEntity.HostileShip.HIBIKI.get(), ShipAttributes.DESTROYER_CLASS);

    }

}
