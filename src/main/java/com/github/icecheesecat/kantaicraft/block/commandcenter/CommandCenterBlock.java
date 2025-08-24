package com.github.icecheesecat.kantaicraft.block.commandcenter;

import com.github.icecheesecat.kantaicraft.block.TwoPartBlock;
import com.github.icecheesecat.kantaicraft.block.patternblock.ComponentBlockEntity;
import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.PlayerKantaiDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class CommandCenterBlock extends TwoPartBlock implements EntityBlock {
    public CommandCenterBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.CONSUME;
        }

        var be = pLevel.getBlockEntity(pPos);
        if (be instanceof CommandCenterBlockEntity ccbe) {
            if (!ccbe.canVisit()) {
                // TODO play sound error
                pPlayer.displayClientMessage(Component.translatable("commandcenter.no_permission"), true);
                return InteractionResult.SUCCESS;
            }

            if (!pPlayer.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
                pPlayer.displayClientMessage(Component.translatable("playerkantaidata.not_present"), true);

            }
            else {
                pPlayer.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent((playerKantaiData -> {
                    ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) pPlayer), new PlayerKantaiDataPacket(playerKantaiData));
                }));

//                NetworkHooks.openScreen();

            }

            return InteractionResult.SUCCESS;

        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ComponentBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return EntityBlock.super.getTicker(pLevel, pState, pBlockEntityType);
    }
}
