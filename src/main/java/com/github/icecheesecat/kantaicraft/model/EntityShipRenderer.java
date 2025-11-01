package com.github.icecheesecat.kantaicraft.model;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandlerCapability;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.equipment.handler.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.model.equipment.EquippableDetailSlots;
import com.github.icecheesecat.kantaicraft.model.equipment.renderer.EquipmentRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityShipRenderer<T extends EntityShip> extends GeoEntityRenderer<T> {

    protected final float scale;
    protected final Color color;
    public static final Color HOSTILE_COLOR = Color.ofRGBA(0.1f, 0.1f, 0.1f, 1.0f);
    public static final Color NORMAL_COLOR = Color.WHITE;
//    protected final ArmingDetailManager armingDetailManager;
    private EquippableDetailSlots equippableDetailSlots;
    private final Map<Integer, EquipmentRenderer> equipmentRenderersCache = new HashMap<>();

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
                        var boneOptional = this.getGeoModel().getBone(armedEquipment.getEquippedOnName());
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

}
