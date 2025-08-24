package com.github.icecheesecat.kantaicraft.menu.commandcenter;

import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlockEntity;
import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiData;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class CommandCenterMenu extends AbstractContainerMenu {

    private PlayerKantaiData playerKantaiData;

    public CommandCenterMenu(int pContainerId, Inventory inventory, CommandCenterBlockEntity ccbe, ContainerLevelAccess access) {
        super(ModMenu.COMMAND_CENTER_MENU.get(), pContainerId);

    }

    public CommandCenterMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, (CommandCenterBlockEntity) Minecraft.getInstance().level.getBlockEntity(extraData.readBlockPos()), ContainerLevelAccess.NULL);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
//        return pPlayer.distanceToSqr();
        return true;
    }

}
