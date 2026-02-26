package com.github.icecheesecat.kantaicraft.model;


import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.model.block.renderer.CommandCenterBlockRenderer;
import com.github.icecheesecat.kantaicraft.model.ship.renderer.AkatsukiRenderer;
import com.github.icecheesecat.kantaicraft.model.ship.renderer.HibikiRenderer;
import com.github.icecheesecat.kantaicraft.model.ship.renderer.IkazuchiRenderer;
import com.github.icecheesecat.kantaicraft.model.ship.renderer.InazumaRenderer;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelEvent {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // entity
//        event.registerLayerDefinition(DestroyerRoClassModel.LAYER_LOCATION, DestroyerRoClassModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Entity
//        event.registerEntityRenderer(ModEntity.PlayerShip.DESTROYER_RO_CLASS.get(), RendererDestroyerRo::new);
        event.registerEntityRenderer(ModEntity.PlayerShip.INAZUMA.get(), (context) -> new InazumaRenderer(context, EntityShipRenderer.NORMAL_COLOR));
        event.registerEntityRenderer(ModEntity.PlayerShip.IKAZUCHI.get(), (context) -> new IkazuchiRenderer(context, EntityShipRenderer.NORMAL_COLOR));
        event.registerEntityRenderer(ModEntity.PlayerShip.HIBIKI.get(), (context) -> new HibikiRenderer(context, EntityShipRenderer.NORMAL_COLOR));
        event.registerEntityRenderer(ModEntity.PlayerShip.AKATSUKI.get(), (context) -> new AkatsukiRenderer(context, EntityShipRenderer.NORMAL_COLOR));

        // Hostile entity
        event.registerEntityRenderer(ModEntity.HostileShip.INAZUMA.get(), (context) -> new InazumaRenderer(context, EntityShipRenderer.HOSTILE_COLOR));
        event.registerEntityRenderer(ModEntity.HostileShip.IKAZUCHI.get(), (context) -> new IkazuchiRenderer(context, EntityShipRenderer.HOSTILE_COLOR));
        event.registerEntityRenderer(ModEntity.HostileShip.HIBIKI.get(), (context) -> new HibikiRenderer(context, EntityShipRenderer.HOSTILE_COLOR));
        event.registerEntityRenderer(ModEntity.HostileShip.AKATSUKI.get(), (context) -> new AkatsukiRenderer(context, EntityShipRenderer.HOSTILE_COLOR));


        // BlockEntity
//        event.registerBlockEntityRenderer(ModBlock.SHIPYARD_BETPYE.get(), ShipyardRenderer::new);
        event.registerBlockEntityRenderer(ModBlock.COMMAND_CENTER_BETYPE.get(), CommandCenterBlockRenderer::new);
    }

}
