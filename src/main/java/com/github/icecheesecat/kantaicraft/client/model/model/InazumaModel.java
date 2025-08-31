package com.github.icecheesecat.kantaicraft.client.model.model;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.client.model.AnimatedModel;
import com.github.icecheesecat.kantaicraft.entity.ship.destroyer.Inazuma;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class InazumaModel<T extends Inazuma> extends AnimatedModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private float scalar = 0.5f;
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KantaiCraft.MODID, "inzauma"), "main");
	private final ModelPart root;
	private final ModelPart bone;
	private final ModelPart spine;
	private final ModelPart body;
	private final ModelPart arm_left;
	private final ModelPart forearm_left;
	private final ModelPart arm_right;
	private final ModelPart forearm_right;
	private final ModelPart badge;
	private final ModelPart head;
	private final ModelPart hair;
	private final ModelPart eyes;
	private final ModelPart left_eye;
	private final ModelPart left_eye_white;
	private final ModelPart left_pupil;
	private final ModelPart right_eye;
	private final ModelPart right_eye_white;
	private final ModelPart right_pupil;
	private final ModelPart right_eye_brown;
	private final ModelPart right_eye_brown_normal;
	private final ModelPart right_eye_brown_shock;
	private final ModelPart right_eye_brown_flat;
	private final ModelPart right_eye_brown_worry;
	private final ModelPart right_eye_brown_raise;
	private final ModelPart right_eye_brown_sad;
	private final ModelPart right_eye_brown_angry;
	private final ModelPart left_eye_brown;
	private final ModelPart left_eye_brown_normal;
	private final ModelPart left_eye_brown_flat;
	private final ModelPart left_eye_brown_flat2;
	private final ModelPart left_eye_brown_worry;
	private final ModelPart left_eye_brown_raise;
	private final ModelPart left_eye_brown_sad;
	private final ModelPart left_eye_brown_angry;
	private final ModelPart expression;
	private final ModelPart happy;
	private final ModelPart round_eyes;
	private final ModelPart lower_body;
	private final ModelPart leg_left;
	private final ModelPart thigh_left;
	private final ModelPart lower_leg_left;
	private final ModelPart leg_right;
	private final ModelPart thigh_right;
	private final ModelPart lower_leg_right;
	private final ModelPart dress;

	public InazumaModel(ModelPart root) {
		this.root = root.getChild("root");
		this.bone = this.root.getChild("bone");
		this.spine = this.bone.getChild("spine");
		this.body = this.spine.getChild("body");
		this.arm_left = this.body.getChild("arm_left");
		this.forearm_left = this.arm_left.getChild("forearm_left");
		this.arm_right = this.body.getChild("arm_right");
		this.forearm_right = this.arm_right.getChild("forearm_right");
		this.badge = this.body.getChild("badge");
		this.head = this.spine.getChild("head");
		this.hair = this.head.getChild("hair");
		this.eyes = this.head.getChild("eyes");
		this.left_eye = this.eyes.getChild("left_eye");
		this.left_eye_white = this.left_eye.getChild("left_eye_white");
		this.left_pupil = this.left_eye.getChild("left_pupil");
		this.right_eye = this.eyes.getChild("right_eye");
		this.right_eye_white = this.right_eye.getChild("right_eye_white");
		this.right_pupil = this.right_eye.getChild("right_pupil");
		this.right_eye_brown = this.head.getChild("right_eye_brown");
		this.right_eye_brown_normal = this.right_eye_brown.getChild("right_eye_brown_normal");
		this.right_eye_brown_shock = this.right_eye_brown.getChild("right_eye_brown_shock");
		this.right_eye_brown_flat = this.right_eye_brown.getChild("right_eye_brown_flat");
		this.right_eye_brown_worry = this.right_eye_brown.getChild("right_eye_brown_worry");
		this.right_eye_brown_raise = this.right_eye_brown.getChild("right_eye_brown_raise");
		this.right_eye_brown_sad = this.right_eye_brown.getChild("right_eye_brown_sad");
		this.right_eye_brown_angry = this.right_eye_brown.getChild("right_eye_brown_angry");
		this.left_eye_brown = this.head.getChild("left_eye_brown");
		this.left_eye_brown_normal = this.left_eye_brown.getChild("left_eye_brown_normal");
		this.left_eye_brown_flat = this.left_eye_brown.getChild("left_eye_brown_flat");
		this.left_eye_brown_flat2 = this.left_eye_brown.getChild("left_eye_brown_flat2");
		this.left_eye_brown_worry = this.left_eye_brown.getChild("left_eye_brown_worry");
		this.left_eye_brown_raise = this.left_eye_brown.getChild("left_eye_brown_raise");
		this.left_eye_brown_sad = this.left_eye_brown.getChild("left_eye_brown_sad");
		this.left_eye_brown_angry = this.left_eye_brown.getChild("left_eye_brown_angry");
		this.expression = this.head.getChild("expression");
		this.happy = this.expression.getChild("happy");
		this.round_eyes = this.expression.getChild("round_eyes");
		this.lower_body = this.bone.getChild("lower_body");
		this.leg_left = this.lower_body.getChild("leg_left");
		this.thigh_left = this.leg_left.getChild("thigh_left");
		this.lower_leg_left = this.thigh_left.getChild("lower_leg_left");
		this.leg_right = this.lower_body.getChild("leg_right");
		this.thigh_right = this.leg_right.getChild("thigh_right");
		this.lower_leg_right = this.thigh_right.getChild("lower_leg_right");
		this.dress = this.lower_body.getChild("dress");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition spine = bone.addOrReplaceChild("spine", CubeListBuilder.create(), PartPose.offset(0.0F, -9.5F, 0.0F));

		PartDefinition body = spine.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 50).addBox(-6.0F, -9.5F, -5.0F, 12.0F, 19.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition arm_left = body.addOrReplaceChild("arm_left", CubeListBuilder.create(), PartPose.offset(-6.005F, -9.2F, -0.5F));

		PartDefinition arm_left_r1 = arm_left.addOrReplaceChild("arm_left_r1", CubeListBuilder.create().texOffs(45, 50).addBox(-4.9744F, -0.0331F, -2.5F, 5.0F, 11.2381F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0619F, 0.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition forearm_left = arm_left.addOrReplaceChild("forearm_left", CubeListBuilder.create(), PartPose.offset(-4.7487F, 11.0506F, -1.7F));

		PartDefinition forearm_left_r1 = forearm_left.addOrReplaceChild("forearm_left_r1", CubeListBuilder.create().texOffs(78, 98).addBox(-4.9744F, 11.205F, -2.5F, 5.0F, 12.7619F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7487F, -11.1125F, 1.7F, 0.0F, 0.0F, 0.1309F));

		PartDefinition arm_right = body.addOrReplaceChild("arm_right", CubeListBuilder.create(), PartPose.offset(5.9328F, -9.3594F, -1.1431F));

		PartDefinition arm_right_r1 = arm_right.addOrReplaceChild("arm_right_r1", CubeListBuilder.create().texOffs(65, 50).addBox(0.0079F, 0.0226F, -1.8236F, 5.0F, 11.1346F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0246F, -0.006F, -0.0333F, 0.0F, 0.0F, -0.1309F));

		PartDefinition forearm_right = arm_right.addOrReplaceChild("forearm_right", CubeListBuilder.create(), PartPose.offset(4.807F, 11.1061F, -1.0569F));

		PartDefinition forearm_right_r1 = forearm_right.addOrReplaceChild("forearm_right_r1", CubeListBuilder.create().texOffs(98, 98).addBox(-2.5F, -0.4327F, -0.8F, 5.0F, 12.8654F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7831F, 0.0513F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition badge = body.addOrReplaceChild("badge", CubeListBuilder.create().texOffs(65, 16).addBox(2.1252F, -3.1905F, 4.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(65, 16).addBox(2.1252F, -1.1905F, 4.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 7.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition badge_r1 = badge.addOrReplaceChild("badge_r1", CubeListBuilder.create().texOffs(68, 17).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6252F, -1.6905F, 5.0F, -0.7854F, 0.0F, -1.5708F));

		PartDefinition badge_r2 = badge.addOrReplaceChild("badge_r2", CubeListBuilder.create().texOffs(68, 17).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6252F, -1.6905F, 5.0F, -0.7854F, 0.0F, -1.5708F));

		PartDefinition badge_r3 = badge.addOrReplaceChild("badge_r3", CubeListBuilder.create().texOffs(68, 17).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.6252F, -1.6905F, 5.0F, -0.7854F, 0.0F, -1.5708F));

		PartDefinition head = spine.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -14.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -23.5F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition hair = head.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(92, 64).addBox(-7.1908F, -6.3629F, -10.3271F, 18.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.8092F, -8.6371F, 1.3271F));

		PartDefinition hair_bonus_r1 = hair.addOrReplaceChild("hair_bonus_r1", CubeListBuilder.create().texOffs(-3, 0).addBox(-3.5F, 0.0F, -1.0F, 8.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.7203F, -2.3982F, 6.6156F, -1.5483F, -0.0175F, -0.48F));

		PartDefinition hair_top_r1 = hair.addOrReplaceChild("hair_top_r1", CubeListBuilder.create().texOffs(92, 0).addBox(-9.0F, -9.0F, 0.0F, 18.0F, 18.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8092F, -6.3629F, -1.3271F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition hair_left_r1 = hair.addOrReplaceChild("hair_left_r1", CubeListBuilder.create().texOffs(92, 0).addBox(-9.0F, -77.0F, -9.0F, 18.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8092F, 70.6371F, -1.3271F, 0.0F, -1.5708F, 0.0F));

		PartDefinition hair_right_r1 = hair.addOrReplaceChild("hair_right_r1", CubeListBuilder.create().texOffs(92, 81).addBox(-9.0F, -77.0F, -9.0F, 18.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8092F, 70.6371F, -1.3271F, 0.0F, 1.5708F, 0.0F));

		PartDefinition hair_front_r1 = hair.addOrReplaceChild("hair_front_r1", CubeListBuilder.create().texOffs(88, 46).addBox(-9.0F, -8.0F, 0.0F, 18.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8092F, 1.6371F, 7.6729F, 0.0F, 3.1416F, 0.0F));

		PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(-4.0F, -2.0F, 8.01F));

		PartDefinition left_eye = eyes.addOrReplaceChild("left_eye", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_eye_white = left_eye.addOrReplaceChild("left_eye_white", CubeListBuilder.create().texOffs(79, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_pupil = left_eye.addOrReplaceChild("left_pupil", CubeListBuilder.create().texOffs(79, 8).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.01F));

		PartDefinition right_eye = eyes.addOrReplaceChild("right_eye", CubeListBuilder.create(), PartPose.offset(8.0F, 0.0F, 0.0F));

		PartDefinition right_eye_white = right_eye.addOrReplaceChild("right_eye_white", CubeListBuilder.create().texOffs(79, 4).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_pupil = right_eye.addOrReplaceChild("right_pupil", CubeListBuilder.create().texOffs(79, 12).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.01F));

		PartDefinition right_eye_brown = head.addOrReplaceChild("right_eye_brown", CubeListBuilder.create(), PartPose.offset(0.0F, 51.0F, 0.0F));

		PartDefinition right_eye_brown_normal = right_eye_brown.addOrReplaceChild("right_eye_brown_normal", CubeListBuilder.create().texOffs(102, 19).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -52.0F, 9.0F));

		PartDefinition right_eye_brown_shock = right_eye_brown.addOrReplaceChild("right_eye_brown_shock", CubeListBuilder.create().texOffs(102, 23).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -52.0F, 9.0F));

		PartDefinition right_eye_brown_flat = right_eye_brown.addOrReplaceChild("right_eye_brown_flat", CubeListBuilder.create().texOffs(102, 27).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -52.0F, 9.0F));

		PartDefinition right_eye_brown_worry = right_eye_brown.addOrReplaceChild("right_eye_brown_worry", CubeListBuilder.create().texOffs(102, 31).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -52.0F, 9.0F));

		PartDefinition right_eye_brown_raise = right_eye_brown.addOrReplaceChild("right_eye_brown_raise", CubeListBuilder.create().texOffs(102, 35).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -52.0F, 9.0F));

		PartDefinition right_eye_brown_sad = right_eye_brown.addOrReplaceChild("right_eye_brown_sad", CubeListBuilder.create().texOffs(102, 39).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -52.0F, 9.0F));

		PartDefinition right_eye_brown_angry = right_eye_brown.addOrReplaceChild("right_eye_brown_angry", CubeListBuilder.create().texOffs(112, 118).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -52.0F, 9.0F));

		PartDefinition left_eye_brown = head.addOrReplaceChild("left_eye_brown", CubeListBuilder.create(), PartPose.offset(0.0F, 51.0F, 0.0F));

		PartDefinition left_eye_brown_normal = left_eye_brown.addOrReplaceChild("left_eye_brown_normal", CubeListBuilder.create().texOffs(110, 19).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -56.0F, 9.01F));

		PartDefinition left_eye_brown_flat = left_eye_brown.addOrReplaceChild("left_eye_brown_flat", CubeListBuilder.create().texOffs(110, 23).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -52.0F, 9.0F));

		PartDefinition left_eye_brown_flat2 = left_eye_brown.addOrReplaceChild("left_eye_brown_flat2", CubeListBuilder.create().texOffs(110, 27).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -52.0F, 9.0F));

		PartDefinition left_eye_brown_worry = left_eye_brown.addOrReplaceChild("left_eye_brown_worry", CubeListBuilder.create().texOffs(110, 31).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -52.0F, 9.0F));

		PartDefinition left_eye_brown_raise = left_eye_brown.addOrReplaceChild("left_eye_brown_raise", CubeListBuilder.create().texOffs(110, 35).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -52.0F, 9.0F));

		PartDefinition left_eye_brown_sad = left_eye_brown.addOrReplaceChild("left_eye_brown_sad", CubeListBuilder.create().texOffs(110, 39).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -52.0F, 9.0F));

		PartDefinition left_eye_brown_angry = left_eye_brown.addOrReplaceChild("left_eye_brown_angry", CubeListBuilder.create().texOffs(120, 118).addBox(-1.0F, -6.0F, 0.01F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -52.0F, 9.0F));

		PartDefinition expression = head.addOrReplaceChild("expression", CubeListBuilder.create(), PartPose.offset(0.0F, 51.0F, 0.0F));

		PartDefinition happy = expression.addOrReplaceChild("happy", CubeListBuilder.create().texOffs(48, 111).addBox(-11.0F, -4.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(48, 111).addBox(-3.0F, -4.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -51.0F, 7.99F));

		PartDefinition round_eyes = expression.addOrReplaceChild("round_eyes", CubeListBuilder.create().texOffs(48, 115).addBox(-6.0F, -2.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(58, 115).addBox(1.0F, -2.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -53.0F, 7.99F));

		PartDefinition lower_body = bone.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(45, 94).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(43, 80).addBox(-7.0F, -2.0F, -5.0F, 14.0F, 2.0F, 10.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition leg_left = lower_body.addOrReplaceChild("leg_left", CubeListBuilder.create(), PartPose.offset(-2.5F, 6.0F, -0.5F));

		PartDefinition thigh_left = leg_left.addOrReplaceChild("thigh_left", CubeListBuilder.create().texOffs(22, 79).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_leg_left = thigh_left.addOrReplaceChild("lower_leg_left", CubeListBuilder.create().texOffs(22, 99).addBox(-2.5F, 0.0793F, -4.4391F, 5.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.9207F, 1.9391F));

		PartDefinition leg_right = lower_body.addOrReplaceChild("leg_right", CubeListBuilder.create(), PartPose.offset(2.5F, 6.0F, -0.5F));

		PartDefinition thigh_right = leg_right.addOrReplaceChild("thigh_right", CubeListBuilder.create().texOffs(2, 79).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_leg_right = thigh_right.addOrReplaceChild("lower_leg_right", CubeListBuilder.create().texOffs(2, 99).addBox(-2.5F, -0.248F, -4.3838F, 5.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.248F, 1.8838F));

		PartDefinition dress = lower_body.addOrReplaceChild("dress", CubeListBuilder.create(), PartPose.offset(0.0F, 6.1375F, -0.0598F));

		PartDefinition dress_back_r1 = dress.addOrReplaceChild("dress_back_r1", CubeListBuilder.create().texOffs(28, 32).mirror().addBox(-7.0F, -2.0F, -1.0F, 14.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, -6.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition dress_right_r1 = dress.addOrReplaceChild("dress_right_r1", CubeListBuilder.create().texOffs(77, 22).mirror().addBox(13.0F, -2.0F, -5.0F, 0.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, -4.0F, 0.0F, 0.0F, -3.1416F, 0.0F));

		PartDefinition dress_right_r2 = dress.addOrReplaceChild("dress_right_r2", CubeListBuilder.create().texOffs(57, 22).addBox(-13.0F, -2.0F, -5.0F, 0.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -4.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition dress_front_r1 = dress.addOrReplaceChild("dress_front_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-7.0F, -2.0F, 1.0F, 14.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 6.0F, 0.0F, 3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		poseStack.pushPose();
		poseStack.scale(scalar, scalar, scalar);
		poseStack.translate(0, 1.5, 0);
		super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//		poseStack.popPose();
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public ModelPart getHead() {
		return this.head;
	}

}