package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

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

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, 40, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(this.access, pPlayer, ModBlock.SHIPYARD_CORE.get());
    }

}
