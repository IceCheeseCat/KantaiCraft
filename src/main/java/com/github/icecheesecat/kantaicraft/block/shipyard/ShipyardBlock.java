package com.github.icecheesecat.kantaicraft.block.shipyard;

import com.github.icecheesecat.kantaicraft.block.facilities.FacilityCoreBlock;
import com.github.icecheesecat.kantaicraft.block.facilities.FacilityPatterns;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ShipyardBlock extends FacilityCoreBlock {

    public ShipyardBlock(Properties pProperties) {
        super(FacilityPatterns.SHIPYARD, pProperties);
    }

    @Override
    protected void rightClickActivity(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

    }

    @Override
    protected <T extends BlockEntity> BlockEntityType<T> getBlockEntityType() {
        return (BlockEntityType<T>) ModBlock.SHIPYARD_BETYPE.get();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ShipyardBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlock.SHIPYARD_BETYPE.get(), ShipyardBlockEntity::tick);
    }

}
