package com.github.icecheesecat.kantaicraft.block.commandcenter;

import com.github.icecheesecat.kantaicraft.block.TwoPartBlock;
import com.github.icecheesecat.kantaicraft.menu.commandcenter.CommandCenterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CommandCenterBlock extends TwoPartBlock {

    private static final Component MENU_TITLE = Component.translatable("command_center_menu");

    public CommandCenterBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        pPlayer.openMenu(this.getMenuProvider(pState, pLevel, pPos));
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((id, inventory, player) ->
            new CommandCenterMenu(id, inventory, ContainerLevelAccess.create(player.level(), pPos))
        , MENU_TITLE);
    }

}
