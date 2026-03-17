package com.github.icecheesecat.kantaicraft.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public interface ItemReplenishBlockTank {

    default boolean doReplenish(BlockEntity blockEntity, ItemStack pItemInHand, Player player, InteractionHand hand) {
        if (pItemInHand.is(this.replenishingItem())) {
            blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent( tank -> {
                for (int i = 0; i < tank.getTanks(); i++) {
                    if (pItemInHand.is(replenishingItem()) &&
                            pItemInHand.getItem() instanceof BucketItem bucketItem) {
                        fillFromBucket(tank, bucketItem, hand, pItemInHand, player);
                    }
                    else if (pItemInHand.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent() ) {
                        pItemInHand.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(iFluidHandler -> {
                            fillFromFluidHandler(tank, iFluidHandler, tank.getFluidInTank(0).getFluid());
                        });
                    }
                }
            });

            blockEntity.setChanged();

            return true;
        }

        return false;
    }

    private void fillFromBucket(@NotNull IFluidHandler tank, BucketItem bucket, InteractionHand pHand, ItemStack pFilledStack, Player player) {
        var filled = tank.fill(new FluidStack(bucket.getFluid(), FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE);
        if (filled == FluidType.BUCKET_VOLUME) {
            tank.fill(new FluidStack(bucket.getFluid(), FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
            Item item = pFilledStack.getItem();
            player.setItemInHand(pHand, ItemUtils.createFilledResult(pFilledStack, player, new ItemStack(Items.BUCKET)));
            player.level().playSound((Player)null, player.blockPosition(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            player.level().gameEvent((Entity)null, GameEvent.FLUID_PLACE, player.blockPosition());
        }
    }

    private void fillFromFluidHandler(@NotNull IFluidHandler tank, @NotNull IFluidHandler itemTank, Fluid fluid) {
        int canFill = tank.fill(new FluidStack(fluid, FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE);
        var drained = itemTank.drain(canFill, IFluidHandler.FluidAction.EXECUTE);
        tank.fill(drained, IFluidHandler.FluidAction.EXECUTE);
    }

    Item replenishingItem();

}
