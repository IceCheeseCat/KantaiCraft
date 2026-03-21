package com.github.icecheesecat.kantaicraft.block.facilities.fuelstation;

import com.github.icecheesecat.kantaicraft.block.facilities.FacilityCoreBlock;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.util.ItemReplenishBlockTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FuelStationBlock extends FacilityCoreBlock implements ItemReplenishBlockTank {
    public FuelStationBlock() {
        super();
    }

    @Override
    protected void rightClickActivity(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (getCoreBlockEntity(pLevel, pPos) instanceof FuelStationBlockEntity fuelStationBlockEntity) {
            if (!this.doReplenish(fuelStationBlockEntity, pPlayer.getItemInHand(pHand), pPlayer, pHand)) {
                pPlayer.sendSystemMessage(fuelStationBlockEntity.getWorkingStatus());
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FuelStationBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlock.FUEL_STATION_BETYPE.get(), FuelStationBlockEntity::tick);
    }

    @Override
    public Item replenishingItem() {
        return Items.LAVA_BUCKET;
    }

}
