package com.github.icecheesecat.shincolle.entity.destroyer.DestroyerRo;

import com.github.icecheesecat.shincolle.Shincolle;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DestroyerRoRenderer<T extends EntityDestroyerRo> extends MobRenderer<T, ModelDestroyerRo<T>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Shincolle.MODID, "textures/entity/destroyer_ro.png");

    public DestroyerRoRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelDestroyerRo<>(context.bakeLayer(ModelDestroyerRo.LAYER_LOCATION)), 0.5f);
    }


    @Override
    public ResourceLocation getTextureLocation(EntityDestroyerRo p_114482_) {
        return TEXTURE;
    }
}
