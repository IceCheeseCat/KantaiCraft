package com.github.icecheesecat.kantaicraft.client.model;

import com.github.icecheesecat.kantaicraft.client.animation.Animations;
import com.github.icecheesecat.kantaicraft.client.animation.FacialAnimations;
import com.github.icecheesecat.kantaicraft.client.animation.util.AnimationUtil;
import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.entity.ship.EmotionState;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

import java.util.HashMap;
import java.util.Map;

public abstract class AnimatedModel<T extends EntityShip> extends HierarchicalModel<T> implements MaskModelColor {

    public abstract ModelPart getHead();
    protected static Map<EmotionState, AnimationDefinition> emotionCorrespondToFacialAnimation = new HashMap<>();

    static {
        emotionCorrespondToFacialAnimation.put(EmotionState.NORMAL, FacialAnimations.Facial_normal);
        emotionCorrespondToFacialAnimation.put(EmotionState.HAPPY, FacialAnimations.Facial_happy);
        emotionCorrespondToFacialAnimation.put(EmotionState.SAD, FacialAnimations.Facial_sad);
        emotionCorrespondToFacialAnimation.put(EmotionState.ANGRY, FacialAnimations.Facial_angry);
        emotionCorrespondToFacialAnimation.put(EmotionState.SHOCK, FacialAnimations.Facial_shock);
        emotionCorrespondToFacialAnimation.put(EmotionState.SERIOUS, FacialAnimations.Facial_serious);
    }

    @Override
    public void setupAnim(EntityShip entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose); // TODO Animation Blend in

        //// General Animation
        AnimationUtil.headLooking(getHead(), headPitch, netHeadYaw);
//        // Breath Animation
        animate(entity.breathAnimationState, Animations.Breath, entity.tickCount);
//        // Idle
        animate(entity.idleAnimationState, Animations.Random_look, entity.tickCount);
//        // Walk
        animate(entity.walkAnimationState, Animations.Walk, entity.tickCount, entity.walkAnimation.speed() * 10.0f);
//        // Run
        animate(entity.runAnimationState, Animations.Run, entity.tickCount,0.75f);
//        // Melee
        animate(entity.attackAnimationState, Animations.Melee_attack, entity.tickCount);

        //// Facial Animation
        // TODO Emotion to Facial
        applyStatic(FacialAnimations.Facial_features);
        animate(entity.emotionAnimationState,
                emotionCorrespondToFacialAnimation.get(entity.getEmotionState()), entity.tickCount);
        animate(entity.blinkAnimationState, Animations.Blink, entity.tickCount);

    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed * getRed(), pGreen * getGreen(), pBlue * getBlue(), pAlpha * getAlpha());
    }
}
