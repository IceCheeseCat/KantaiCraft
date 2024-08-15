package com.github.icecheesecat.shincolle.entity.util.client;

import com.github.icecheesecat.shincolle.Shincolle;
import com.github.icecheesecat.shincolle.entity.destroyer.DestroyerRo.DestroyerRoRenderer;
import com.github.icecheesecat.shincolle.entity.destroyer.DestroyerRo.ModelDestroyerRo;
import com.github.icecheesecat.shincolle.init.ModEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Shincolle.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class RendererEvent {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelDestroyerRo.LAYER_LOCATION, ModelDestroyerRo::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntity.DestroyerRo.get(), DestroyerRoRenderer::new);
    }

}
