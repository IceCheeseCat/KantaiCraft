package com.github.icecheesecat.kantaicraft.block;

import com.github.icecheesecat.kantaicraft.block.componentUtil.ComponentPattern;
import com.github.icecheesecat.kantaicraft.block.componentUtil.PatternType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ComponentBlock extends Block {

    Map<PatternType, ComponentPattern> allowPatterns = new HashMap<>();

    public ComponentBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        this.putIfAbsentPatterns();
        for (var ap: allowPatterns.entrySet()) {
            List<BlockPos> blockPoses = ap.getValue().findPattern(pLevel, pPos);
            if (blockPoses != null) {
                BlockPos startPoint = blockPoses.get(0);
                LevelChunk levelChunk = pLevel.getChunkAt(startPoint);
                levelChunk.blockent
                var blockEntity = new ShipyardBlockEntity(startPoint, pState);
                levelChunk.addAndRegisterBlockEntity(blockEntity);
                for (int i = 1; i < blockPoses.size(); i++) {
                    levelChunk.getBlockEntities().put(blockPoses.get(i), blockEntity);
                }

            }
//            if (blockPoses != null){
//                System.out.println("==============================");
//                for (var pos: blockPoses) {
//                    System.out.println(pos + " -> " + pLevel.getBlockState(pos).getBlock());
//                }
//                System.out.println("==============================");
//                System.out.println("Success Pattern");
//            }
//            else {
//                System.out.println("Failed Pattern");
//            }
        }


        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    protected abstract void putIfAbsentPatterns();

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.PASS;
        if (pHand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }


        LevelChunk levelChunk = pLevel.getChunkAt(pPos);
        var blockEntities = levelChunk.getBlockEntities();
        var be = blockEntities.get(pPos);

        if (be instanceof ShipyardBlockEntity shipyardBlockEntity) {
            NetworkHooks.openScreen((ServerPlayer) pPlayer, shipyardBlockEntity, (extraData) -> {
                extraData.writeBlockPos(shipyardBlockEntity.getBlockPos());
            });
        }

        return InteractionResult.FAIL;
    }
}
