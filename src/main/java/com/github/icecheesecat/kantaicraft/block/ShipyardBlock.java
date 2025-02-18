package com.github.icecheesecat.kantaicraft.block;

import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuiltData;
import com.github.icecheesecat.kantaicraft.item.ShipBlueprintData;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShipyardBlock extends BaseEntityBlock {

    public ShipyardBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ShipyardBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == ModBlock.SHIPYARD_BETYPE.get() ? ShipyardBlockEntity::tick : null;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.PASS;
        if (pHand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (pLevel.getBlockEntity(pPos) instanceof ShipyardBlockEntity shipyardBlockEntity) {
            NetworkHooks.openScreen((ServerPlayer) pPlayer, shipyardBlockEntity, (extraData) -> {
                extraData.writeBlockPos(pPos);
            });
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pLevel.getBlockEntity(pPos) instanceof ShipyardBlockEntity shipyardBlockEntity) {
            double x = pPos.getCenter().x;
            double y = pPos.getCenter().y;
            double z = pPos.getCenter().z;

            // drop itemHandler
            shipyardBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(
                    itemHandler -> {

                        for (int i = 0; i < itemHandler.getSlots(); i++) {
                            ItemStack itemStack = itemHandler.getStackInSlot(i);
                            if (itemStack.isEmpty()) continue;
                            ItemEntity itemEntity = new ItemEntity(pLevel, x, y, z, itemStack);
                            pLevel.addFreshEntity(itemEntity);
                        }
                    }
            );

            // drop built data
            List<BuiltData> builtData = shipyardBlockEntity.getBuiltData();
            for (var data: builtData) {
                ItemStack itemStack = data.getData().createItemStack();
                ItemEntity itemEntity = new ItemEntity(pLevel, x, y, z, itemStack);
                pLevel.addFreshEntity(itemEntity);
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
