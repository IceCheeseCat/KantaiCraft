package com.github.icecheesecat.kantaicraft.model;

import com.github.icecheesecat.kantaicraft.capability.equipment.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.model.equipment.EquippableDetailSlots;
import com.github.icecheesecat.kantaicraft.model.equipment.renderer.EquipmentRenderer;
import com.github.icecheesecat.kantaicraft.util.AxisRotation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityShipRenderer<T extends EntityShip> extends GeoEntityRenderer<T> {

    public static final Color HOSTILE_COLOR = Color.ofRGBA(0.1f, 0.1f, 0.1f, 1.0f);
    public static final Color NORMAL_COLOR = Color.WHITE;
    private static final int RED = FastColor.ARGB32.color(255, 255, 0, 0);
    private static final int BLUE = FastColor.ARGB32.color(255, 0, 0, 255);
    public static boolean debug = false;
    protected final float scale;
    protected final Color color;
    private final Map<Integer, EquipmentRenderer> equipmentRenderersCache = new HashMap<>();
//    protected final ArmingDetailManager armingDetailManager;
    private EquippableDetailSlots equippableDetailSlots;

    public EntityShipRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model, float scale, float shadowRadius, Color color) {
        super(renderManager, model);
        this.scale = scale;
        this.shadowRadius = shadowRadius;
        this.color = color;
        this.equippableDetailSlots = this.defineDetailSlots();
//        this.armingDetailManager = new ArmingDetailManager();
//        this.armingDetailManager.setupBodyPartPosition(model.getBakedModel(model.getModelResource(null)), defineBodyPartOffsetToWeapon());
    }

    protected abstract EquippableDetailSlots defineDetailSlots();

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return this.model.getTextureResource(animatable);
    }

    @Override
    public void render(@NotNull T entity, float entityYaw, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(this.scale, this.scale, this.scale);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        // render equipped weapon here
        this.renderBodyPartWeapon(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.scale(1.0f/this.scale, 1.0f/this.scale, 1.0f/this.scale);

        if (debug) {
            this.renderDebugConeSensor(entity, poseStack, bufferSource, partialTick);
        }

        poseStack.popPose();
    }

    @Override
    public Color getRenderColor(T animatable, float partialTick, int packedLight) {
        return this.color;
    }

    protected void renderBodyPartWeapon(@NotNull T entity, float entityYaw, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {

//        entity.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
//                equipmentHandler -> {
//                    cachingBakedModels(equipmentHandler);
//
//                    var boneOptional = this.getGeoModel().getBone("root");
//                    if (boneOptional.isEmpty()) throw new RuntimeException("Didn't find EntityShip model with root hierarchy");
//                    recursivelyRenderWeapon(equipmentHandler, boneOptional.get(), entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
//
//                }
//        );

        entity.getCapability(EquipmentHandlerCapability.TOKEN).ifPresent(
                equipmentHandler -> {
                    cachingBakedModels(equipmentHandler);

                    for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
                        poseStack.pushPose();

                        var equipment = equipmentHandler.getEquipment(i);
                        var armedEquipment = equipmentHandler.getArmedEquipment(i);
                        var boneOptional = this.getGeoModel().getBone(armedEquipment.getEquippableBoneName());
                        if (boneOptional.isPresent()) {
//                            poseStack.scale(100, 100, 100);
//                            poseStack.mulPoseMatrix(boneOptional.get().getWorldSpaceMatrix());
//                            poseStack.mulPoseMatrix(boneOptional.get().getLocalSpaceMatrix());
                            poseStack.translate(boneOptional.get().getLocalPosition().x, boneOptional.get().getLocalPosition().y, boneOptional.get().getLocalPosition().z);
                            this.entityRotation(entity, poseStack, partialTick);
                            RenderUtils.rotateMatrixAroundBone(poseStack, boneOptional.get());
                            RenderUtils.rotateMatrixAroundBone(poseStack, boneOptional.get().getParent());
//                            poseStack.translate(0.0f, -0.5f, 0);
//                            poseStack.mulPoseMatrix(boneOptional.get().getLocalSpaceMatrix());
                            this.equipmentRenderersCache.get(i).defaultRender(poseStack, equipment, bufferSource, null, null, entityYaw, partialTick, packedLight);
                        }

                        poseStack.popPose();
                    }

                }
        );

    }

    protected void entityRotation(@NotNull T livingEntity, PoseStack poseStack, float partialTick) {
        float lerpBodyRot = livingEntity == null ? 0.0F : Mth.rotLerp(partialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);
        float ageInTicks = (float)livingEntity.tickCount + partialTick;
        this.applyRotations(livingEntity, poseStack, ageInTicks, lerpBodyRot, partialTick);
    }

    protected void recursivelyRenderWeapon(EquipmentHandler equipmentHandler, GeoBone bone, @NotNull T entity, float entityYaw, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);
        RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        RenderUtils.scaleMatrixForBone(poseStack, bone);
        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

        for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
            var equipment = equipmentHandler.getEquipment(i);
            var armedEquipment = equipmentHandler.getArmedEquipment(i);
//            if (bone.getName().equals(armedEquipment.getArmedBodyPart().name())) {

                poseStack.pushPose();
                this.equipmentRenderersCache.get(i).defaultRender(poseStack, equipment, bufferSource, null, null, entityYaw, partialTick, packedLight);
                poseStack.popPose();

//            }
        }


        for (var childBone: bone.getChildBones()) {
            recursivelyRenderWeapon(equipmentHandler, childBone, entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
        poseStack.popPose();
    }

    public void cachingBakedModels(EquipmentHandler equipmentHandler) {
        for (int i = 0; i < equipmentHandler.getSlotSize(); i++) {
            if (equipmentHandler.isDirty(i)) {
                EquipmentRenderer renderer = (EquipmentRenderer) EquipmentManager.createEquipmentRenderer(equipmentHandler.getEquipment(i).getId());
                assert renderer != null;

                this.equipmentRenderersCache.put(i, renderer);
            }
        }
    }

    private void renderDebugConeSensor(T entity, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, float partialTicks) {
        int ray_count = 20;
        double angle = 5.0d;
//        Vec3 lookAngle = calculateViewVector(-entity.getXRot(), entity.yHeadRot);
        Vec3 viewVector = entity.getViewVector(partialTicks);
        Vec3 eyeStart = new Vec3(0, entity.getEyeHeight(), 0);
        Vec3 eyeEnd = eyeStart.add(viewVector.multiply(10, 10, 10));
        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.debugLineStrip(5.0d));

        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        float f3 = (float) FastColor.ARGB32.alpha(RED) / 255.0F;
        float f = (float) FastColor.ARGB32.red(RED) / 255.0F;
        float f1 = (float) FastColor.ARGB32.green(RED) / 255.0F;
        float f2 = (float) FastColor.ARGB32.blue(RED) / 255.0F;
        float f7 = (float) FastColor.ARGB32.alpha(BLUE) / 255.0F;
        float f4 = (float) FastColor.ARGB32.red(BLUE) / 255.0F;
        float f5 = (float) FastColor.ARGB32.green(BLUE) / 255.0F;
        float f6 = (float) FastColor.ARGB32.blue(BLUE) / 255.0F;

        vertexConsumer.vertex(matrix4f, (float) eyeStart.x, (float) eyeStart.y, (float) eyeStart.z).color(f, f1, f2, f3).endVertex();
        vertexConsumer.vertex(matrix4f, (float) eyeEnd.x, (float) eyeEnd.y, (float) eyeEnd.z).color(f, f1, f2, f3).endVertex();


        for (int i = 0; i < ray_count; i++) {
            float deg = 360.0f/ray_count * i;

            Vec3 A = viewVector.cross(new Vec3(0,1,0));
            Vec3 lifted = AxisRotation.getRotatedVectorAroundAnAxis(Axis.of(A.toVector3f()), viewVector, 5.0f);
            Vec3 rotated = AxisRotation.getRotatedVectorAroundAnAxis(Axis.of(viewVector.toVector3f()), lifted, deg);
            Vec3 rotated_end = eyeStart.add(rotated.multiply(10, 10, 10));
            vertexConsumer.vertex(matrix4f, (float) eyeStart.x, (float) eyeStart.y, (float) eyeStart.z).color(f4, f5, f6, f7).endVertex();
            vertexConsumer.vertex(matrix4f, (float) rotated_end.x, (float) rotated_end.y, (float) rotated_end.z).color(f4, f5, f6, f7).endVertex();
        }



        poseStack.popPose();
    }

}
