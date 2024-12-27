package com.github.icecheesecat.kantaicraft.entity.plane;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererTestPlane<T extends BasicEntityPlane> extends MobRenderer<T, ModelTestPlane<T>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(KantaiCraft.MODID, "textures/entity/destroyer_ro.png");

    public RendererTestPlane(EntityRendererProvider.Context context) {
        super(context, new ModelTestPlane<>(context.bakeLayer(ModelTestPlane.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return TEXTURE;
    }

}

