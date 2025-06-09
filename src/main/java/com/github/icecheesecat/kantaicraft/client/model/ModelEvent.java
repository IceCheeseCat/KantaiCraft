package com.github.icecheesecat.kantaicraft.client.model;


import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.renderer.ShipyardRenderer;
import com.github.icecheesecat.kantaicraft.client.model.renderer.RendererDestroyerRo;
import com.github.icecheesecat.kantaicraft.client.model.renderer.RendererInazuma;
import com.github.icecheesecat.kantaicraft.menu.shipyard.ShipyardScreen;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import com.github.icecheesecat.kantaicraft.menu.ship.ShipScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelEvent {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelDestroyerRo.LAYER_LOCATION, ModelDestroyerRo::createBodyLayer);
        event.registerLayerDefinition(ModelInazuma.LAYER_LOCATION, ModelInazuma::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Entity
        event.registerEntityRenderer(ModEntity.DestroyerRoClass.get(), RendererDestroyerRo::new);
        event.registerEntityRenderer(ModEntity.Inazuma.get(), RendererInazuma::new);

        // BlockEntity
        event.registerBlockEntityRenderer(ModBlock.SHIPYARD_BETPYE.get(), ShipyardRenderer::new);
    }



}
