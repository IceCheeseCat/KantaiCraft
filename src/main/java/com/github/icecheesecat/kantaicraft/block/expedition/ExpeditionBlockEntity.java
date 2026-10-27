package com.github.icecheesecat.kantaicraft.block.expedition;

import com.github.icecheesecat.kantaicraft.block.TwoPart;
import com.github.icecheesecat.kantaicraft.block.TwoPartBlock;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class ExpeditionBlockEntity extends BlockEntity implements GeoBlockEntity {

    private static final double RENDER_PADDING = 0.5;
    private static final AABB EMPTY_RENDER_BOUNDS = new AABB(0, 0, 0, 0, 0, 0);

    private AnimatableInstanceCache m_animatableInstanceCache = new SingletonAnimatableInstanceCache(this);

    public ExpeditionBlockEntity(BlockPos p_pos, BlockState p_state) {
        super(ModBlock.EXPEDITION_BETYPE.get(), p_pos, p_state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar p_controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return m_animatableInstanceCache;
    }

    public void setAnimatableInstanceCache(AnimatableInstanceCache p_animatableInstanceCache) {
        m_animatableInstanceCache = p_animatableInstanceCache;
    }

    @Override
    public AABB getRenderBoundingBox() {
        if (getBlockState().getValue(TwoPartBlock.TWO_PART) == TwoPart.Back) {
            return EMPTY_RENDER_BOUNDS;
        }

        AABB front = new AABB(getBlockPos());
        AABB back = new AABB(getBlockPos().relative(getBlockState().getValue(TwoPartBlock.FACING)));

        return front.minmax(back).inflate(RENDER_PADDING);
    }
}
