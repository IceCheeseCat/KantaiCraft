package com.github.icecheesecat.kantaicraft.block.facilities;

import com.github.icecheesecat.kantaicraft.block.BlockStateProperties;
import com.github.icecheesecat.kantaicraft.block.facilities.pattern.FacilityPattern;
import com.github.icecheesecat.kantaicraft.block.facilities.pattern.FindPatternResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public abstract class FacilityCoreBlock extends FacilityBlock {
    private final FacilityPattern.Getter patternGetter;
    public FacilityCoreBlock(FacilityPattern.Getter patternGetter, Properties pProperties) {
        super(pProperties);
        this.patternGetter = patternGetter;
    }

    protected FacilityPattern getFacilityPattern() {
        return patternGetter.get();
    }

    public static InteractionResult openMenu(Level pLevel, BlockPos blockPos, Player pPlayer, BlockState pState) {
        if (isWorkingFacility(pState)) {
            BlockPos corePos = getFacilityCoreBlockPos(pLevel, blockPos);
            MenuProvider menuProvider = (MenuProvider) pLevel.getBlockEntity(corePos);
            if (menuProvider != null) {
                pPlayer.openMenu(menuProvider);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        FindPatternResult result = this.getFacilityPattern().findPattern(pLevel, pPos);
        if (result.isSuccess() && allFacilityBlocksCanWork(pLevel, result.getBlockPoses())) {
            System.out.println(result.getDirection());
            onPlaceLinkFacilityBlocks(pLevel, result);
        }

        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    private boolean allFacilityBlocksCanWork(Level level, List<BlockPos> blocks) {
        return blocks.stream().noneMatch(pos -> level.getBlockState(pos).getValue(BlockStateProperties.WORKING_FACILITY));
    }

    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>)pTicker : null;
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof FacilityCoreBlockEntity facilityBlockEntity) {
            if (!pLevel.isClientSide) {
                ItemStack itemstack = new ItemStack(this);
                BlockItem.setBlockEntityData(itemstack, this.getBlockEntityType(), facilityBlockEntity.saveExtraData());

                ItemEntity itementity = new ItemEntity(pLevel, (double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, itemstack);
                itementity.setDefaultPickUpDelay();
                pLevel.addFreshEntity(itementity);
            }
        }

    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            if (pState.getValue(BlockStateProperties.WORKING_FACILITY)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        if (isWorkingFacility(pState)) {
            this.rightClickActivity(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }

        return InteractionResult.SUCCESS;
    }

    protected abstract void rightClickActivity(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit);

    protected abstract  <T extends BlockEntity> BlockEntityType<T> getBlockEntityType();
}
