package com.github.icecheesecat.kantaicraft.block.basic;

import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.PatternType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

public class ComponentBlock extends BaseEntityBlock {
    public ComponentBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ComponentBlockEntity(pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pLevel.getBlockEntity(pPos) instanceof ComponentBlockEntity cbe) {
            CoreBlock coreBlock = cbe.getCoreBlock(pLevel);
            if (coreBlock != null) {
                var coreState = pLevel.getBlockState(cbe.getCorePos());
                coreBlock.onRemoveLinked(coreState, pLevel, cbe.getCorePos());
            }
        }


        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.PASS;
        if (pLevel.getBlockEntity(pPos) instanceof ComponentBlockEntity cbe) {

            CoreBlock coreBlock = cbe.getCoreBlock(pLevel);
            if (coreBlock != null) {
                var coreState = pLevel.getBlockState(cbe.getCorePos());
                return coreBlock.use(coreState, pLevel, cbe.getCorePos(), pPlayer, pHand, pHit);
            }

        }

        return InteractionResult.PASS;
    }
}
