package com.github.icecheesecat.kantaicraft.client.model.renderer;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.client.model.model.HostileInazumaModel;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.hostile.HostileInazuma;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererHostileInazuma<T extends HostileInazuma> extends MobRenderer<T, HostileInazumaModel<T>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(KantaiCraft.MODID, "textures/entity/inazuma.png");

    public RendererHostileInazuma(EntityRendererProvider.Context context) {
        super(context, new HostileInazumaModel<>(context.bakeLayer(HostileInazumaModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return TEXTURE;
    }

}
