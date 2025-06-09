package com.github.icecheesecat.kantaicraft.client.model.renderer;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.client.model.ModelInazuma;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.EntityInazuma;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

public class RendererInazuma<T extends EntityInazuma> extends MobRenderer<T, ModelInazuma<T>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(KantaiCraft.MODID, "textures/entity/inazuma.png");

    public RendererInazuma(EntityRendererProvider.Context context) {
        super(context, new ModelInazuma(context.bakeLayer(ModelInazuma.LAYER_LOCATION)), 0.5f);
    }


    @Override
    public ResourceLocation getTextureLocation(EntityInazuma p_114482_) {
        return TEXTURE;
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
