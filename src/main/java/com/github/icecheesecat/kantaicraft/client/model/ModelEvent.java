package com.github.icecheesecat.kantaicraft.client.model;


import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.renderer.ShipyardRenderer;
import com.github.icecheesecat.kantaicraft.client.model.model.HostileInazumaModel;
import com.github.icecheesecat.kantaicraft.client.model.model.InazumaModel;
import com.github.icecheesecat.kantaicraft.client.model.model.DestroyerRoClassModel;
import com.github.icecheesecat.kantaicraft.client.model.renderer.RendererDestroyerRo;
import com.github.icecheesecat.kantaicraft.client.model.renderer.RendererHostileInazuma;
import com.github.icecheesecat.kantaicraft.client.model.renderer.RendererInazuma;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelEvent {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // entity
        event.registerLayerDefinition(DestroyerRoClassModel.LAYER_LOCATION, DestroyerRoClassModel::createBodyLayer);
        event.registerLayerDefinition(InazumaModel.LAYER_LOCATION, InazumaModel::createBodyLayer);
        // hostile entity
        event.registerLayerDefinition(HostileInazumaModel.LAYER_LOCATION, HostileInazumaModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Entity
        event.registerEntityRenderer(ModEntity.PlayerShip.DESTROYER_RO_CLASS.get(), RendererDestroyerRo::new);
        event.registerEntityRenderer(ModEntity.PlayerShip.INAZUMA.get(), RendererInazuma::new);

        // hostile entity
        event.registerEntityRenderer(ModEntity.HostileShip.INAZUMA.get(), RendererHostileInazuma::new);

        // BlockEntity
        event.registerBlockEntityRenderer(ModBlock.SHIPYARD_BETPYE.get(), ShipyardRenderer::new);
    }



}
