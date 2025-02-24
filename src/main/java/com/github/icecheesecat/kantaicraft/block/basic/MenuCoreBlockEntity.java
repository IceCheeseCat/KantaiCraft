package com.github.icecheesecat.kantaicraft.block.basic;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

public abstract class MenuCoreBlockEntity extends CoreBlockEntity implements MenuProvider {

    public MenuCoreBlockEntity(BlockEntityType<?> beType, BlockPos pPos, BlockState pBlockState) {
        super(beType, pPos, pBlockState);
    }

    public void openMenu(ServerPlayer player) {
        if (!this.canUse) return;
        NetworkHooks.openScreen(player, this, this::menuExtraData);
    }

    public abstract void menuExtraData(FriendlyByteBuf buf);

}
