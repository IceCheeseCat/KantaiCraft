package com.github.icecheesecat.kantaicraft.block.patternblock;

import com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil.BlockStateProperties;
import com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil.PatternType;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ComponentBlock extends PatternBlock {

    public ComponentBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
            this.getStateDefinition().any()
                .setValue(BlockStateProperties.PATTERN_TYPE, PatternType.NONE)
        );
    }

    @Override
    protected void putIfAbsentPatterns() {
        if (!this.allowPatterns.containsKey(PatternType.SHIPYARD)) {
            this.allowPatterns.put(PatternType.SHIPYARD, ComponentPattern.Builder.start(2, 2, 2)
                    .addBlock(0, 0, 0, ModBlock.FLOOR.get())
                    .addBlock(0, 0, 1, ModBlock.FLOOR.get())
                    .addBlock(1, 0, 0, Blocks.WATER)
                    .addBlock(1,0,1, Blocks.WATER)
                    .addBlock(0, 1, 1, ModBlock.CRANE.get())
                    .addBlock(0,1,0, ModBlock.SHIPYARD_CORE.get())
                    .build());
        }
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
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        this.putIfAbsentPatterns();
        List<BlockPos> blockPoses = checkPattern(pLevel, pPos);
        if (blockPoses != null) {
            for (var pos: blockPoses) {
                if (pLevel.getBlockState(pos).getBlock() instanceof CoreBlock coreBlock) {
                    coreBlock.componentBlockPlaced(pLevel, pos, blockPoses);
                    break;
                }
            }
        }

        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
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
