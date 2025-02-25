package com.github.icecheesecat.kantaicraft.block.basic;

import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.PatternType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class CoreBlock extends BaseEntityBlock implements IComponentDrops {

    protected Map<PatternType, ComponentPattern> allowPatterns = new HashMap<>();
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
            pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.PATTERN_TYPE, this.patternType));
        }

        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    private List<BlockPos> checkPattern(Level pLevel, BlockPos pPos) {
        for (var ap: allowPatterns.entrySet()) {
            List<BlockPos> blockPoses = ap.getValue().findPattern(pLevel, pPos);

            if (blockPoses != null) return blockPoses;
        }

        return null;
    }

    private void setupPatternBlocks(Level level, BlockPos corePos, List<BlockPos> blockPoses) {
        CoreBlockEntity coreBlockEntity = (CoreBlockEntity) level.getBlockEntity(corePos);
        for (var pos: blockPoses) {
            if (level.getBlockState(pos).getBlock() instanceof ComponentBlock) {
                if (level.getBlockEntity(pos) instanceof ComponentBlockEntity cbe) {
                    System.out.println("Linked:" + level.getBlockState(pos).getBlock() + ", " + pos);
                    cbe.setCorePos(corePos);
                    coreBlockEntity.addLinked(pos);
                }

                level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(BlockStateProperties.PATTERN_TYPE, this.patternType));
            }
        }

        coreBlockEntity.setCanUse(true);
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

    protected abstract void putIfAbsentPatterns();

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;

        var be = pLevel.getBlockEntity(pPos);
        if (be instanceof MenuCoreBlockEntity menuCoreBlockEntity) {
            if (!menuCoreBlockEntity.canUse()) return InteractionResult.PASS;
            menuCoreBlockEntity.openMenu((ServerPlayer) pPlayer);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

}
