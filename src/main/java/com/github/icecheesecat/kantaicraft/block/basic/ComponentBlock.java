package com.github.icecheesecat.kantaicraft.block.basic;

import com.github.icecheesecat.kantaicraft.block.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.basic.componentUtil.PatternType;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuiltData;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ComponentBlock extends BaseEntityBlock {

    protected Map<PatternType, ComponentPattern> allowPatterns = new HashMap<>();
    public static final EnumProperty<PatternType> PATTERN_TYPE = EnumProperty.create("pattern_type", PatternType.class);
    public static final BooleanProperty IS_CORE = BooleanProperty.create("is_core");

    public ComponentBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(PATTERN_TYPE, PatternType.NONE)
                        .setValue(IS_CORE, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(PATTERN_TYPE, IS_CORE);
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
                BlockPos corePos = blockPoses.get(0);
                linkCoreBlockStates(pLevel, corePos, ap.getKey(), blockPoses);
                linkCoreBlockEntities(pLevel, corePos, ap.getKey(), blockPoses);
            }
        }

        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        if (pLevel.getBlockEntity(pPos) instanceof ComponentBlockEntity cbe) {
            if (cbe.insidePatternPoses(pNeighborPos)) {
                cbe.resetAllNeighbors();
            }
        }

        super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);
    }

    private void linkCoreBlockStates(Level level, BlockPos corePos, PatternType patternType, List<BlockPos> poses) {
        for (var pos: poses) {
            BlockState blockState = level.getBlockState(pos);
            if (!(blockState.getBlock() instanceof ComponentBlock)) continue;
            if (pos.equals(corePos)) {
                blockState.setValue(PATTERN_TYPE, patternType).setValue(IS_CORE, true);
            }
            else {
                blockState.setValue(PATTERN_TYPE, patternType).setValue(IS_CORE, false);
            }
        }
    }

    private void linkCoreBlockEntities(Level level, BlockPos corePos, PatternType patternType, List<BlockPos> poses) {
        for (var pos: poses) {
            if (pos.equals(corePos)) {
                level.setBlockEntity(this.createCoreBlockEntity(patternType, corePos, level.getBlockState(corePos)));
            }

            if (level.getBlockEntity(pos) instanceof ComponentBlockEntity cbe) {
                cbe.setCorePos(corePos);
                cbe.setNeighbors(poses);
            }
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getValue(PATTERN_TYPE) != PatternType.NONE) {
            var be = pLevel.getBlockEntity(pPos);
            if (be instanceof ComponentBlockEntity cbe) {
                cbe.resetAllNeighbors();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    private BlockEntity createCoreBlockEntity(PatternType patternType, BlockPos corePos, BlockState blockState) {
        return switch (patternType) {
            case SHIPYARD -> new ShipyardBlockEntity(corePos, blockState);
            default -> null;
        };
    }

    protected abstract void putIfAbsentPatterns();

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.PASS;
        if (pHand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if (pLevel.getBlockEntity(pPos) instanceof ComponentBlockEntity componentBlockEntity) {
            BlockEntity be = componentBlockEntity.getCoreBlockEntity(pLevel);
            if (be instanceof ShipyardBlockEntity shipyardBlockEntity) {
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
        if (pState.getValue(IS_CORE)) {
            return switch (pState.getValue(PATTERN_TYPE)) {
                case SHIPYARD -> ShipyardBlockEntity::tick;
                default -> null;
            };
        }

        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ComponentBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return super.getPistonPushReaction(state);
    }
}
