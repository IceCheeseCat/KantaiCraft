package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.capability.EquipmentProvider;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.faction.FactionTag;
import com.github.icecheesecat.kantaicraft.faction.FactionTagCapability;
import com.github.icecheesecat.kantaicraft.faction.LevelFactionCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilitiesEvent {

    @SubscribeEvent
    public static void onEntityAttachingCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof BasicEntityShip ship) {
            if (!ship.getCapability(EquipmentProvider.EQUIPMENT_HANDLER_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.equipment_handler"), new EquipmentProvider(4));
            }
        }

        if (event.getObject() instanceof LivingEntity livingEntity) {
            if (!livingEntity.getCapability(FactionTagCapability.FACTION_TAG).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.faction_tag"), new FactionTagCapability());
            }
        }

    }

    @SubscribeEvent
    public static void onLevelAttachingCapability(AttachCapabilitiesEvent<Level> event) {
        Level level = event.getObject();
        if (!level.getCapability(LevelFactionCapability.FACTION).isPresent()) {
            event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.faction"), new LevelFactionCapability());
        }

    }

}
