package com.github.icecheesecat.kantaicraft.block.facilities;

import com.github.icecheesecat.kantaicraft.block.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;
import java.util.function.Consumer;

public abstract class FacilityCoreBlock extends FacilityBlock {
    public FacilityCoreBlock() {
        super();
    }

    public static InteractionResult openMenu(Level pLevel, BlockPos blockPos, Player pPlayer, BlockState pState, Consumer<FriendlyByteBuf> bufferWriter) {
        if (isWorkingFacility(pState)) {
            BlockPos corePos = getFacilityCoreBlockPos(pLevel, blockPos);
            MenuProvider menuProvider = (MenuProvider) pLevel.getBlockEntity(corePos);
            if (menuProvider != null) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, menuProvider, bufferWriter);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }


    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>)pTicker : null;
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        var drops = super.getDrops(pState, pParams);
        BlockEntity blockentity = (BlockEntity)pParams.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        for (ItemStack drop : drops) {
            if (drop.is(this.asItem())) {
                BlockItem.setBlockEntityData(drop, blockentity.getType(), blockentity.saveWithoutMetadata());
                break;
            }
        }

        return drops;
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
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    protected abstract void rightClickActivity(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit);

}
