package com.github.icecheesecat.kantaicraft.block.commandcenter;

import com.github.icecheesecat.kantaicraft.block.TwoPart;
import com.github.icecheesecat.kantaicraft.block.TwoPartBlock;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class CommandCenterBlockEntity extends BlockEntity implements GeoBlockEntity {

    protected static final RawAnimation QUILL_ANIMATION = RawAnimation.begin().thenPlay("quill");
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public CommandCenterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.COMMAND_CENTER_BETYPE.get(), pPos, pBlockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, this::idleAnimation));
    }

    protected <E extends CommandCenterBlockEntity> PlayState idleAnimation(final AnimationState<E> state) {
        if (this.level.getGameTime() % 60 == 0) {
            state.setAnimation(QUILL_ANIMATION);
            state.getController().forceAnimationReset();
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public AABB getRenderBoundingBox() {
        if (this.getBlockState().getValue(TwoPartBlock.TWO_PART) == TwoPart.Back) return new AABB(0,0,0,0,0,0);
        Direction facing = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        AABB front = new AABB(this.getBlockPos());
        AABB back = new AABB(this.getBlockPos().relative(facing));
        AABB combined = combineAABB(front, back).inflate(0.5f);

        ;
//        box = box.move(this.getBlockPos());
        return combined;
    }

    private AABB combineAABB(AABB box1, AABB box2) {
        return new AABB(
                Math.min(box1.minX, box2.minX),
                Math.min(box1.minY, box2.minY),
                Math.min(box1.minZ, box2.minZ),
                Math.max(box1.maxX, box2.maxX),
                Math.max(box1.maxY, box2.maxY),
                Math.max(box1.maxZ, box2.maxZ)
        );
    }


}
