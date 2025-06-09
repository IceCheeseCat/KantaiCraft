package com.github.icecheesecat.kantaicraft.client;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public final class KeyBindings {

    public static final KeyMapping debugEntityRootRotation = new KeyMapping(
            "key.debug_entity_root_rotation",
            InputConstants.KEY_J,
            "key.categories." + KantaiCraft.MODID
    );

    @Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Event {

        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(debugEntityRootRotation);
        }

    }

    @Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeEvent {

        @SubscribeEvent
        public static void registerKeys(TickEvent.ClientTickEvent event) {
            if (debugEntityRootRotation.isDown()) {
                if (Minecraft.getInstance().level != null) {
                    var entities = Minecraft.getInstance().level.getEntities(Minecraft.getInstance().player, Minecraft.getInstance().player.getBoundingBox().inflate(10.0d));
                    for (var entity: entities) {
                        if (entity instanceof BasicEntityShip ship) {
                            ship.debugAnim();
                        }
                    }
                }
            }
        }

    }

}
