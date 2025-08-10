package com.github.icecheesecat.kantaicraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class TwoPartBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<TwoBlockPart> TWO_PART = EnumProperty.create("two_block_part", TwoBlockPart.class);
    protected static final VoxelShape BASE = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d);

    public TwoPartBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TWO_PART, TwoBlockPart.Back));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, TWO_PART);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BASE;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos blockpos = pContext.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(direction);
        Level level = pContext.getLevel();
        return level.getBlockState(blockpos1).canBeReplaced(pContext) && level.getWorldBorder().isWithinBounds(blockpos1) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (pLevel.isClientSide) return;
        BlockPos blockpos = pPos.relative(pState.getValue(FACING));
        pLevel.setBlock(blockpos, pState.setValue(TWO_PART, TwoBlockPart.Front), 3);
        pLevel.blockUpdated(pPos, Blocks.AIR);
        pState.updateNeighbourShapes(pLevel, pPos, 3);

    }

    private static Direction getNeighbourDirection(TwoBlockPart pPart, Direction pDirection) {
        return pPart == TwoBlockPart.Back ? pDirection : pDirection.getOpposite();
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pFacing == getNeighbourDirection(pState.getValue(TWO_PART), pState.getValue(FACING))) {
            if (!(pFacingState.is(this) && pFacingState.getValue(TWO_PART) != pState.getValue(TWO_PART)))
                return Blocks.AIR.defaultBlockState();
            else return pState;
        }
        else {
            return super.updateShape(pState, pFacing, pFacingState, pLevel, pPos, pNeighborPos);
        }
    }
}
