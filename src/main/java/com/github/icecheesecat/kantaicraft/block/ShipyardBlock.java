package com.github.icecheesecat.kantaicraft.block;

import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

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
        if (pLevel.getBlockEntity(pPos) instanceof ShipyardBlockEntity shipyardBlockEntity) {
            NetworkHooks.openScreen((ServerPlayer) pPlayer, shipyardBlockEntity, (extraData) -> {
                // blueprint slots
                extraData.writeNbt(shipyardBlockEntity.blueprintSlots.get(0).serializeNBT());
                extraData.writeNbt(shipyardBlockEntity.blueprintSlots.get(1).serializeNBT());
                extraData.writeNbt(shipyardBlockEntity.blueprintSlots.get(2).serializeNBT());
                extraData.writeNbt(shipyardBlockEntity.blueprintSlots.get(3).serializeNBT());

                // built slots
                extraData.writeByte(shipyardBlockEntity.builtShips.size());
                for (int i = 0; i < shipyardBlockEntity.builtShips.size(); i++) {
                    extraData.writeNbt(shipyardBlockEntity.builtShips.get(i).serializeNBT());
                }
            });
        }

        return InteractionResult.CONSUME;
    }
}
