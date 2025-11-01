package com.github.icecheesecat.kantaicraft.model.ship.renderer;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.model.ship.model.DestroyerRoClassModel;
import com.github.icecheesecat.kantaicraft.entityship.entity.destroyer.DestroyerRoClass;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererDestroyerRo<T extends DestroyerRoClass> extends MobRenderer<T, DestroyerRoClassModel<T>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(KantaiCraft.MODID, "textures/entity/destroyer_ro.png");

    public RendererDestroyerRo(EntityRendererProvider.Context context) {
        super(context, new DestroyerRoClassModel<>(context.bakeLayer(DestroyerRoClassModel.LAYER_LOCATION)), 0.5f);
    }


    @Override
    public ResourceLocation getTextureLocation(DestroyerRoClass p_114482_) {
        return TEXTURE;
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
