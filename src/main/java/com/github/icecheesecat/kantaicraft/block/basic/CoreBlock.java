package com.github.icecheesecat.kantaicraft.block.basic;

import com.github.icecheesecat.kantaicraft.block.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.block.ShipyardCoreBlock;
import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.PatternType;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CoreBlock extends BaseEntityBlock implements IComponentDrops {

    protected Map<PatternType, ComponentPattern> allowPatterns = new HashMap<>();
    private final PatternType patternType;

    public CoreBlock(Properties pProperties, PatternType patternType) {
        super(pProperties);
        this.patternType = patternType;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    /**
     * If onPlace has a pattern, set one of the BlockEntity as core and its BlockState to IS_CORE.
     * All block entities link reference to core and neighbor, all block states set PatternType
     */

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        this.putIfAbsentPatterns();
        for (var ap: allowPatterns.entrySet()) {
            List<BlockPos> blockPoses = ap.getValue().findPattern(pLevel, pPos);
            // blockPoses 0 as core
            if (blockPoses != null) {
                pLevel.setBlockEntity(this.createCoreBlockEntity(pPos, pState));

                linkComponents(pLevel, pPos, blockPoses);
            }
        }

        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);
    }

    private void linkComponents(Level level, BlockPos corePos, List<BlockPos> poses) {
        CoreBlockEntity coreBlockEntity = (CoreBlockEntity) level.getBlockEntity(corePos);
        for (var pos: poses) {

            if (level.getBlockEntity(pos) instanceof ComponentBlockEntity cbe) {
                System.out.println("Linked:" + level.getBlockState(pos).getBlock() + ", " + pos);
                cbe.setCorePos(corePos);
                coreBlockEntity.addLinked(pos);
            }
        }

    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        this.onRemoveLinked(pState, pLevel, pPos);
        this.dropAllWhenPatternDestryed(pLevel, pPos);
        pLevel.removeBlockEntity(pPos);

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    public void onRemoveLinked(BlockState pState, Level pLevel, BlockPos pPos) {
        var be = pLevel.getBlockEntity(pPos);
        if (be instanceof CoreBlockEntity coreBlockEntity) {
            coreBlockEntity.resetLinked();
        }
    }

    private BlockEntity createCoreBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new
    }

    protected abstract void putIfAbsentPatterns();

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.PASS;

        if (pLevel.getBlockEntity(pPos) instanceof CoreBlockEntity coreBlockEntity) {
            if (coreBlockEntity instanceof ShipyardBlockEntity shipyardBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, shipyardBlockEntity, (extraData) -> {
                    extraData.writeBlockPos(shipyardBlockEntity.getBlockPos());
                });
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return switch (patternType) {
            case SHIPYARD -> ShipyardBlockEntity::tick;
            default -> null;
        };
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return null;
    }



}
