package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.BuildSlot;
import com.github.icecheesecat.kantaicraft.block.BuildSlotContainer;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class ShipyardMenu extends AbstractContainerMenu {

    private NonNullList<BuildSlot> buildSlots;
    private BuildSlotContainer buildSlotContainer;
    private Inventory inventory;

    // server side
    public ShipyardMenu(int pContainerId, Inventory inventory, NonNullList<BuildSlot> buildSlots) {
        super(ModMenu.SHIPYARD_MENU.get(), pContainerId);
        this.buildSlots = buildSlots;
        this.buildSlotContainer = new BuildSlotContainer(this.buildSlots);
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

        // build slot container
        for (int slotIndex = 0; slotIndex < this.buildSlots.size(); slotIndex++) {
            this.addSlot(new Slot(this.buildSlotContainer, slotIndex, 10 + slotIndex * 18, 20));
        }

    }

    // client side
    public ShipyardMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, readBuildSlots(extraData));

        this.buildSlotContainer = new BuildSlotContainer(this.buildSlots);

    }

    private static NonNullList<BuildSlot> readBuildSlots(FriendlyByteBuf extraData) {
        var nList = NonNullList.withSize(4, new BuildSlot());
        for (var slot: nList) {
            slot.deserializeNBT(extraData.readNbt());
        }

        return nList;
    }

    public void tickBuildSlots() {
        for (var slot: this.buildSlots) {
            if (slot.done()) {
                slot.removeBlueprint();
            }
            slot.doProgress();
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot slot = this.slots.get(pIndex);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        if (slot.container instanceof Inventory inventory) {
            int firstEmpty = this.buildSlotContainer.hasEmptySlotAt();
            ItemStack itemStack = this.buildSlotContainer.getItem(pIndex);
            if (firstEmpty == -1) {
                return itemStack;
            }
            // check itemstack could be inserted to container
            if (!this.buildSlotContainer.canInsertAt(firstEmpty, itemStack)) {
                return itemStack;
            }

            this.buildSlotContainer.setItem(firstEmpty, itemStack);

        }

        if (slot.container instanceof BuildSlotContainer bsc) {
            ItemStack itemStack = bsc.getItem(pIndex);
            int inventoryFreeSlot = this.inventory.getFreeSlot();
            if (!itemStack.isEmpty() && inventoryFreeSlot != -1 ) {
                this.getSlot(inventoryFreeSlot)
            }

            return itemStack;
        }
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }
}
