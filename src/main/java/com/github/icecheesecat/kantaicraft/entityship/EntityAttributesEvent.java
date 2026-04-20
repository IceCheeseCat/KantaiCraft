package com.github.icecheesecat.kantaicraft.entityship;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.attribute.shipAttributes.ShipAttributes;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributesEvent {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

//        event.put(ModEntity.PlayerShip.DESTROYER_RO_CLASS.get(), ShipAttributes.Destroyer.DEFAULT);
//        event.put(ModEntity.HostileShip.DESTROYER_RO_CLASS.get(), ShipAttributes.Destroyer.DEFAULT);
//        event.put(ModEntity.PlayerShip.DESTROYER_I_CLASS.get(), ShipAttributes.Destroyer.DEFAULT);
//        event.put(ModEntity.HostileShip.DESTROYER_I_CLASS.get(), ShipAttributes.Destroyer.DEFAULT);
        event.put(ModEntity.INAZUMA.get(), ShipAttributes.Destroyer.DEFAULT);
        event.put(ModEntity.HOSTILE_INAZUMA.get(), ShipAttributes.Destroyer.DEFAULT);
        event.put(ModEntity.IKAZUCHI.get(), ShipAttributes.Destroyer.DEFAULT);
        event.put(ModEntity.HOSTILE_IKAZUCHI.get(), ShipAttributes.Destroyer.DEFAULT);
        event.put(ModEntity.HIBIKI.get(), ShipAttributes.Destroyer.DEFAULT);
        event.put(ModEntity.HOSTILE_HIBIKI.get(), ShipAttributes.Destroyer.DEFAULT);
        event.put(ModEntity.AKATSUKI.get(), ShipAttributes.Destroyer.DEFAULT);
        event.put(ModEntity.HOSTILE_AKATSUKI.get(), ShipAttributes.Destroyer.DEFAULT);

    }

}
