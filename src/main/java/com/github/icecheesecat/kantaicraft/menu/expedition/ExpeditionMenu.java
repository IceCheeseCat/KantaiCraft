package com.github.icecheesecat.kantaicraft.menu.expedition;

import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class ExpeditionMenu extends AbstractContainerMenu {

    private ContainerLevelAccess m_access;

    public ExpeditionMenu(int p_containerId, Inventory p_inventory, ContainerLevelAccess p_access) {
        super(ModMenu.EXPEDITION_MENU.get(), p_containerId);
        m_access = p_access;
    }

    public ExpeditionMenu(int p_containerId, Inventory p_inventory, FriendlyByteBuf p_extraData) {
        this(p_containerId, p_inventory,
                ContainerLevelAccess.create(p_inventory.player.level(), p_extraData.readBlockPos()));
    }

    public ContainerLevelAccess getAccess() {
        return m_access;
    }

    @Override
    public ItemStack quickMoveStack(Player p_player, int p_index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player p_player) {
        return stillValid(getAccess(), p_player, ModBlock.EXPEDITION.get());
    }
}
