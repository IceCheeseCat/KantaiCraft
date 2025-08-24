package com.github.icecheesecat.kantaicraft.capability;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.faction.FactionTagCapability;
import com.github.icecheesecat.kantaicraft.faction.LevelFactionCapability;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilitiesEvent {

    @SubscribeEvent
    public static void onEntityAttachingCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof BasicEntityShip ship) {
            if (!ship.getCapability(EquipmentHandlerCapability.TOKEN).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.equipment_handler"), new EquipmentHandlerCapability(4));
            }
        }

        if (event.getObject() instanceof LivingEntity livingEntity) {
            if (!livingEntity.getCapability(FactionTagCapability.FACTION_TAG).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.faction_tag"), new FactionTagCapability());
            }
        }

        if (event.getObject() instanceof Player player) {
//            if (player instanceof ServerPlayer serverPlayer) {
//                if (!serverPlayer.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
//                    event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.player_kantai_data"), new PlayerKantaiDataCapability(player));
//                }
//            }
//            if (player instanceof LocalPlayer localPlayer) {
//                if (!localPlayer.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
//                    event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.player_kantai_data"), new PlayerKantaiDataCapability(player));
//                }
//            }

        }
    }

    @SubscribeEvent
    public static void onLevelAttachingCapability(AttachCapabilitiesEvent<Level> event) {
        Level level = event.getObject();
        if (!level.getCapability(LevelFactionCapability.FACTION).isPresent()) {
            event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.faction"), new LevelFactionCapability());
        }
    }

    @SubscribeEvent
    public static void onBlockEntityAttach(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof ShipyardBlockEntity shipyardBlockEntity) {
            if (!shipyardBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.ship_blueprint_stack_handler"), new ShipBlueprintStackHandlerCapability(shipyardBlockEntity.processShipSize));
            }
        }
    }

    @SubscribeEvent
    public static void onItemStackAttach(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().is(ModItem.SHIP_BLUEPRINT.get())) {
            event.addCapability(new ResourceLocation(KantaiCraft.MODID, "capability.ship_blueprint_data"), new ShipBlueprintCapability());
        }
    }

}
