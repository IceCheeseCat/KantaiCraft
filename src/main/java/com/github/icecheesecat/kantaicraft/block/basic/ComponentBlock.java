package com.github.icecheesecat.kantaicraft.block.basic;

import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.PatternType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ComponentBlock extends BaseEntityBlock {
    public ComponentBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
            this.getStateDefinition().any()
                .setValue(BlockStateProperties.PATTERN_TYPE, PatternType.NONE)
        );
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        if (pState.getValue(BlockStateProperties.PATTERN_TYPE) == PatternType.NONE) {
            return RenderShape.MODEL;
        }
        return RenderShape.INVISIBLE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.PATTERN_TYPE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ComponentBlockEntity(pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pNewState.getBlock().equals(Blocks.AIR)) {
            if (pLevel.getBlockEntity(pPos) instanceof ComponentBlockEntity cbe) {
                CoreBlock coreBlock = cbe.getCoreBlock(pLevel);
                BlockPos corePos = cbe.getCorePos();
                if (coreBlock != null) {
                    coreBlock.onPatternComponentRemoved(pLevel, corePos);
                }
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);

        }
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;
        if (pLevel.getBlockEntity(pPos) instanceof ComponentBlockEntity cbe) {
            if (cbe.getCoreBlockEntity(pLevel) instanceof  MenuCoreBlockEntity menuCoreBlockEntity) {
                menuCoreBlockEntity.openMenu((ServerPlayer) pPlayer);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
