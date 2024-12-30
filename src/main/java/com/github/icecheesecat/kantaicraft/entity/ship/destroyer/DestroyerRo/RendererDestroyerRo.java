package com.github.icecheesecat.kantaicraft.entity.ship.destroyer.DestroyerRo;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererDestroyerRo<T extends EntityDestroyerRo> extends MobRenderer<T, ModelDestroyerRo<T>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(KantaiCraft.MODID, "textures/entity/destroyer_ro.png");

    public RendererDestroyerRo(EntityRendererProvider.Context context) {
        super(context, new ModelDestroyerRo<>(context.bakeLayer(ModelDestroyerRo.LAYER_LOCATION)), 0.5f);
    }


    @Override
    public ResourceLocation getTextureLocation(EntityDestroyerRo p_114482_) {
        return TEXTURE;
    }
}
