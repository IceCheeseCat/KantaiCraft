package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.ShipBlueprintStackHandler;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ShipyardMenu extends AbstractContainerMenu {
    private Inventory inventory;
    private ContainerLevelAccess access;
    ShipyardBlockEntity shipyardBlockEntity;

    // server side
    public ShipyardMenu(int pContainerId, Inventory inventory, ShipyardBlockEntity shipyardBlockEntity, ContainerLevelAccess access) {
        super(ModMenu.SHIPYARD_MENU.get(), pContainerId);

        this.inventory = inventory;

        // Player inventory show in menu
        int i = 0;
        // inventory
        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        // hot bar
        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 161 + i));
        }

        this.shipyardBlockEntity = shipyardBlockEntity;
        // blueprint itemhandler slot
        shipyardBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(
                stackHandler -> {
                    for (int index = 0; index < stackHandler.getSlots(); index++) {
                        this.addSlot(new SlotItemHandler(stackHandler, index, 20 * index + 9, 20));
                    }
                }
        );


        this.access = access;
    }

    // client side
    public ShipyardMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, (ShipyardBlockEntity) Minecraft.getInstance().level.getBlockEntity(extraData.readBlockPos()), ContainerLevelAccess.NULL);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot clickedSlot = this.getSlot(pIndex);
        if (!clickedSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack copy = clickedSlot.getItem().copy();
        // hotbar and inventory
        int startIndex;
        int endIndex;
        if (pIndex < 36) {
            startIndex = 36;
            endIndex = 40;
        }
        else {
            startIndex = 0;
            endIndex = 36;
        }

        boolean found = false;
        for (int i = startIndex; i < endIndex; i++) {
            Slot slot = this.getSlot(i);

            ItemStack ret = slot.safeInsert(clickedSlot.getItem());
            if (ret.isEmpty()) {
                clickedSlot.setByPlayer(ret);
                found = true;
                break;
            }
        }

        if (found) {
            return ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;


    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(this.access, pPlayer, ModBlock.SHIPYARD.get());
    }

}
