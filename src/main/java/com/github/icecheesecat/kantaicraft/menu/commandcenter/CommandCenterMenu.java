package com.github.icecheesecat.kantaicraft.menu.commandcenter;

import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class CommandCenterMenu extends AbstractContainerMenu {
    final Player player;
    ContainerLevelAccess access;

    public CommandCenterMenu(int pContainerId, Inventory inventory, ContainerLevelAccess access) {
        super(ModMenu.COMMAND_CENTER_MENU.get(), pContainerId);
        this.player = inventory.player;
        this.access = access;
    }

    public CommandCenterMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, ContainerLevelAccess.NULL);
//        ModPacketHandler.INSTANCE.sendToServer(new PlayerKantaiDataPacket.Request(this.player.getId()));
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(access, pPlayer, ModBlock.COMMAND_CENTER.get());
    }
}
