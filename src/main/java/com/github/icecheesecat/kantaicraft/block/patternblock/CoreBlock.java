package com.github.icecheesecat.kantaicraft.block.patternblock;

import com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil.BlockStateProperties;
import com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil.IComponentDrops;
import com.github.icecheesecat.kantaicraft.block.patternblock.componentUtil.PatternType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public abstract class CoreBlock extends PatternBlock implements IComponentDrops {

    private final PatternType patternType;

    public CoreBlock(Properties pProperties, PatternType patternType) {
        super(pProperties);
        this.patternType = patternType;
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
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return super.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return super.getCollisionShape(pState, pLevel, pPos, pContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.PATTERN_TYPE);
    }

    /**
     * If onPlace has a pattern, set one of the BlockEntity as core and its BlockState to IS_CORE.
     * All block entities link reference to core and neighbor, all block states set PatternType
     */

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        this.putIfAbsentPatterns();
        var blockPoses = this.checkPattern(pLevel, pPos);
        if (blockPoses != null) {
            this.setupPatternBlocks(pLevel, pPos, blockPoses);
//            pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.PATTERN_TYPE, this.patternType));
        }

        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    public void componentBlockPlaced(Level level, BlockPos corePos, List<BlockPos> poses) {
        this.setupPatternBlocks(level, corePos, poses);
//        level.setBlockAndUpdate(corePos, level.getBlockState(corePos).setValue(BlockStateProperties.PATTERN_TYPE, this.patternType));
    }

    private void setupPatternBlocks(Level level, BlockPos corePos, List<BlockPos> blockPoses) {
        CoreBlockEntity coreBlockEntity = (CoreBlockEntity) level.getBlockEntity(corePos);
        for (var pos: blockPoses) {
            if (level.getBlockState(pos).getBlock() instanceof ComponentBlock) {
                if (level.getBlockEntity(pos) instanceof ComponentBlockEntity cbe) {
//                    System.out.println("Linked:" + level.getBlockState(pos).getBlock() + ", " + pos);
                    cbe.setCorePos(corePos);
                    coreBlockEntity.addLinked(pos);
                }

                level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(BlockStateProperties.PATTERN_TYPE, this.patternType));
            }
        }

        coreBlockEntity.setCanUse(true);
        level.setBlockAndUpdate(corePos, level.getBlockState(corePos).setValue(BlockStateProperties.PATTERN_TYPE, this.patternType));
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pNewState.getBlock().equals(Blocks.AIR)) {
            this.coreBlockRemoved(pLevel, pPos, pState, pNewState, pMovedByPiston);
        }
    }

    private void coreBlockRemoved(Level level, BlockPos blockPos, BlockState blockState, BlockState newState, boolean movedByPiston) {
        this.removeLinks(level, blockPos);
        this.dropAllWhenPatternDestryed(level, blockPos);

        super.onRemove(blockState, level, blockPos, newState, movedByPiston);
    }

    /**
     * Remove linking and blockStates
     */
    public void onPatternComponentRemoved(Level level, BlockPos blockPos) {
        this.removeLinks(level, blockPos);
        level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(BlockStateProperties.PATTERN_TYPE, PatternType.NONE));
        if (level.getBlockEntity(blockPos) instanceof CoreBlockEntity coreBlockEntity) {
            coreBlockEntity.setCanUse(false);
        }
    }

    public void removeLinks(Level pLevel, BlockPos pPos) {
        var be = pLevel.getBlockEntity(pPos);
        if (be instanceof CoreBlockEntity coreBlockEntity) {
            coreBlockEntity.resetLinksAndBlockStates();
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;

        var be = pLevel.getBlockEntity(pPos);
        if (be instanceof MenuProvider menuProvider) {
            NetworkHooks.openScreen((ServerPlayer) pPlayer, menuProvider, buf -> {
                buf.writeBlockPos(pPos);
            });
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

}
