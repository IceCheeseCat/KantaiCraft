package com.github.icecheesecat.kantaicraft.block.shipyard;

import com.github.icecheesecat.kantaicraft.block.facilities.FacilityCoreBlock;
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

    public ShipyardBlock() {
        super();
    }

    @Override
    protected void rightClickActivity(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        openMenu(pLevel, pPos, pPlayer, pState, buf -> buf.writeBlockPos(pPos));
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
