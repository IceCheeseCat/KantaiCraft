package com.github.icecheesecat.kantaicraft.entity.destroyer.DestroyerRo;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelDestroyerRo<T extends EntityDestroyerRo> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KantaiCraft.MODID, "destroyer_ro"), "main");
    private final ModelPart Body;
    private final ModelPart Head;
    private final ModelPart Tail;
    private final ModelPart Idk;

    public ModelDestroyerRo(ModelPart root) {
        this.Body = root.getChild("Body");
        this.Head = root.getChild("Head");
        this.Tail = root.getChild("Tail");
        this.Idk = root.getChild("Idk");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body2_r1 = Body.addOrReplaceChild("body2_r1", CubeListBuilder.create().texOffs(0, 86).addBox(-6.0F, -7.0F, -7.5F, 12.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.0F, 14.5F, -0.0873F, 0.0F, 0.0F));

        PartDefinition body1_r1 = Body.addOrReplaceChild("body1_r1", CubeListBuilder.create().texOffs(56, 60).addBox(-7.0F, -8.0F, -7.5F, 14.0F, 15.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.0F, 3.5F, -0.2182F, 0.0F, 0.0F));

        PartDefinition body0_r1 = Body.addOrReplaceChild("body0_r1", CubeListBuilder.create().texOffs(0, 57).addBox(-8.0F, -9.0F, -7.5F, 16.0F, 17.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -17.0F, -6.5F, -0.2182F, 0.0F, 0.0F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 26).addBox(-10.0F, -9.0F, -8.0F, 20.0F, 18.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(49, 40).addBox(-9.0F, -5.0F, -16.0F, 18.0F, 3.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(59, 11).addBox(-8.0F, 5.0F, -14.0F, 16.0F, 5.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-9.0F, -12.0F, -18.0F, 18.0F, 7.0F, 19.0F, new CubeDeformation(0.0F))
                .texOffs(55, 0).addBox(-7.0F, 2.0F, -12.0F, 14.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(44, 87).addBox(-7.0F, -2.0F, -12.0F, 14.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, -17.0F));

        PartDefinition Tail = partdefinition.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(86, 87).addBox(-5.0F, -5.0F, -8.1667F, 10.0F, 9.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(96, 60).addBox(-4.0F, -4.0F, -2.1667F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(86, 31).addBox(-3.0F, -3.0F, 3.8333F, 6.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 25.1667F, -0.0121F, -0.0096F, 0.0F));

        PartDefinition Idk = partdefinition.addOrReplaceChild("Idk", CubeListBuilder.create().texOffs(97, 0).addBox(-2.5F, -10.0F, 3.0F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(64, 97).addBox(-4.0F, -9.0F, 17.0F, 8.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition tape0_r1 = Idk.addOrReplaceChild("tape0_r1", CubeListBuilder.create().texOffs(53, 12).addBox(0.0F, -1.0F, -12.0F, 0.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -6.1907F, -1.4834F, -0.4919F, -0.1603F, 0.1487F));

        PartDefinition tape01_r1 = Idk.addOrReplaceChild("tape01_r1", CubeListBuilder.create().texOffs(44, 49).addBox(0.0F, -1.0F, -4.0F, 0.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -6.4238F, 8.712F, -2.5745F, -0.0945F, 0.1898F));

        PartDefinition stand1_r1 = Idk.addOrReplaceChild("stand1_r1", CubeListBuilder.create().texOffs(44, 97).addBox(-4.0F, -10.0F, -1.0F, 8.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.8F, 21.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition tape01_r2 = Idk.addOrReplaceChild("tape01_r2", CubeListBuilder.create().texOffs(44, 51).addBox(0.0F, -1.0F, -4.0F, 0.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -6.4238F, 8.712F, -2.5745F, 0.0945F, -0.1898F));

        PartDefinition tape0_r2 = Idk.addOrReplaceChild("tape0_r2", CubeListBuilder.create().texOffs(53, 14).addBox(0.0F, -1.0F, -12.0F, 0.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -6.1907F, -1.4834F, -0.4919F, 0.1603F, -0.1487F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(EntityDestroyerRo entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Idk.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}