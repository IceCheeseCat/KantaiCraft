package com.github.icecheesecat.kantaicraft.block.example;

import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class LDLibBlock extends BaseEntityBlock {

    public LDLibBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                .dynamicShape()
                .requiresCorrectToolForDrops()
                .strength(5.0f, 6.0f)
                .isValidSpawn(((pState, pLevel, pPos, pValue) -> false))
                .pushReaction(PushReaction.BLOCK));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            var be = pLevel.getBlockEntity(pPos);
            if (be instanceof LDLibBlockEntity ldLibBlockEntity) {
                ldLibBlockEntity.addRight(1);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if (pLevel.isClientSide) {
            return;
        }
        var be = pLevel.getBlockEntity(pPos);
        if (be instanceof LDLibBlockEntity ldLibBlockEntity) {
            ldLibBlockEntity.addLeft(1);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new LDLibBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlock.LDLIB_BETYPE.get(), LDLibBlockEntity::tick);
    }
}
