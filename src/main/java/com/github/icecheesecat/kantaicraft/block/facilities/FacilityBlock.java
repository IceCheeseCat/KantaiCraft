package com.github.icecheesecat.kantaicraft.block.facilities;

import com.github.icecheesecat.kantaicraft.block.BlockStateProperties;
import com.github.icecheesecat.kantaicraft.block.facilities.pattern.FacilityPattern;
import com.github.icecheesecat.kantaicraft.block.facilities.pattern.FindPatternResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO fix lava breaking it (nah i will not fix this, since getCollisionShape is the same method as fluid colliding check method)
 */

public class FacilityBlock extends Block implements EntityBlock {

    private List<FacilityPattern> ALL_PATTERNS;

    public FacilityBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                .dynamicShape()
                .requiresCorrectToolForDrops()
                .strength(5.0f, 6.0f)
                .isValidSpawn(((pState, pLevel, pPos, pValue) -> false))
                .pushReaction(PushReaction.BLOCK));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(BlockStateProperties.IS_FACILITY_CORE, false)
                .setValue(BlockStateProperties.WORKING_FACILITY, false)
                .setValue(BlockStateProperties.FACILITY_FACING, Direction.EAST));
    }

    private void createPatterns() {
        if (ALL_PATTERNS == null) {
            ALL_PATTERNS = new ArrayList<>();
            FacilityPatterns.PATTERN_GETTERS.getEntries().forEach(getterRegistryObject -> {
                ALL_PATTERNS.add(getterRegistryObject.get().get());
            });
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.WORKING_FACILITY, BlockStateProperties.IS_FACILITY_CORE, BlockStateProperties.FACILITY_FACING);
    }

    public static boolean isFacilityCoreBlock(BlockState blockState) {
        return blockState.getValue(BlockStateProperties.IS_FACILITY_CORE);
    }

    public static boolean isWorkingFacility(BlockState blockState) {
        return blockState.getValue(BlockStateProperties.WORKING_FACILITY);
    }

    public static @Nullable BlockPos getFacilityCoreBlockPos(Level level, BlockPos blockPos) {
        if (!level.getBlockState(blockPos).getValue(BlockStateProperties.WORKING_FACILITY)) return null;
        if (level.getBlockState(blockPos).getValue(BlockStateProperties.IS_FACILITY_CORE)) return blockPos;
        return level.getBlockEntity(blockPos) instanceof FacilityBlockEntity facilityBlockEntity ? facilityBlockEntity.getCorePos() : null;
    }

    public static @Nullable BlockEntity getCoreBlockEntity(Level level, BlockPos blockPos) {
        BlockPos corePos = getFacilityCoreBlockPos(level, blockPos);
        if (corePos!= null) {
            return level.getBlockEntity(corePos);
        }
        return null;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if (pLevel.isClientSide) return;
        this.createPatterns();

        this.ALL_PATTERNS.forEach(facilityPattern -> {
            FindPatternResult result = facilityPattern.findPattern(pLevel, pPos);
            if (result.isSuccess() && allFacilityBlocksCanWork(pLevel, result.getBlockPoses())) {
                System.out.println(result.getDirection());
                onPlaceLinkFacilityBlocks(pLevel, result);
            }
        });

    }

    private boolean allFacilityBlocksCanWork(Level level, List<BlockPos> blocks) {
        return blocks.stream().noneMatch(pos -> level.getBlockState(pos).getValue(BlockStateProperties.WORKING_FACILITY));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (pLevel.isClientSide) {
            if (pState.getValue(BlockStateProperties.WORKING_FACILITY)) {
                return InteractionResult.SUCCESS;
            }
            else {
                return InteractionResult.PASS;
            }
        }

        if (isWorkingFacility(pState)) {
            BlockPos corePos = getFacilityCoreBlockPos(pLevel, pPos);
            return pLevel.getBlockState(corePos).getBlock().use(pLevel.getBlockState(corePos), pLevel, corePos, pPlayer, pHand, pHit);
        }

        return InteractionResult.PASS;

    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        onRemoveUnlinkFacilityBlockAndBreakBlock(pLevel, pPos);

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    protected void onRemoveUnlinkFacilityBlockAndBreakBlock(Level pLevel, BlockPos pPos) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof FacilityCoreBlockEntity coreEntity) {
            breakAndDropAllFacilityBlocks(pLevel, coreEntity.getLinkedFacilityBlocks());
        }
        else if (blockEntity instanceof FacilityBlockEntity facilityBlockEntity) {
            FacilityCoreBlockEntity be = facilityBlockEntity.getCoreBlockEntity();
            if (be != null) {
                breakAndDropAllFacilityBlocks(pLevel, be.getLinkedFacilityBlocks());
            }
        }
    }

    private void breakAndDropAllFacilityBlocks(Level level, List<BlockPos> blockPoses) {
        for (var pos: blockPoses) {
            level.destroyBlock(pos, true);
        }
    }


    protected void onPlaceLinkFacilityBlocks(Level level, FindPatternResult result) {
        if (!result.isSuccess()) return;

        result.getBlockPoses().forEach(pos -> {
            BlockState blockState1 = level.getBlockState(pos)
                    .setValue(BlockStateProperties.WORKING_FACILITY, true)
                    .setValue(BlockStateProperties.FACILITY_FACING, result.getDirection());
            if (pos == result.getCore()) {
                blockState1 = blockState1.setValue(BlockStateProperties.IS_FACILITY_CORE, true);
            }
            level.setBlock(pos, blockState1, 3);
            if (level.getBlockEntity(pos) instanceof FacilityBlockEntity facilityBlockEntity) {
                facilityBlockEntity.setCorePos(result.getCore());
            }
        });

        if (level.getBlockEntity(result.getCore()) instanceof FacilityCoreBlockEntity facilityCoreBlockEntity) {
            facilityCoreBlockEntity.setup(result);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return isWorkingFacility(pState) ? isFacilityCoreBlock(pState) ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FacilityBlockEntity(pPos, pState);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(BlockStateProperties.WORKING_FACILITY)) {
            return Shapes.empty();
        }

        return pState.getShape(pLevel, pPos);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        if (pState.getValue(BlockStateProperties.WORKING_FACILITY)) {
            return Shapes.empty();
        }

        return Shapes.block();
    }

}
