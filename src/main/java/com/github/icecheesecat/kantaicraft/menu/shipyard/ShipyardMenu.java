package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShipyardMenu extends AbstractContainerMenu {
    List<BlueprintSlot> blueprintSlots = new ArrayList<>();
    private Container shipyardContainer;
    private ShipyardBlockEntity shipyardBlockEntity;
    private ContainerLevelAccess access;

    // server side
    public ShipyardMenu(int pContainerId, Inventory inventory, ShipyardBlockEntity shipyardBlockEntity, ContainerLevelAccess access) {
        super(ModMenu.SHIPYARD_MENU.get(), pContainerId);

        this.shipyardContainer = shipyardBlockEntity;
        this.shipyardBlockEntity = shipyardBlockEntity;

        // Player inventory show in menu
        int inventoryX = 8;
        int inventoryY = 110;
        int inventoryHotbarY = 168;
        // inventory
        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inventory, j1 + l * 9 + 9, inventoryX + j1 * 18, inventoryY + l * 18));
            }
        }
        // hot bar
        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inventory, i1, inventoryX + i1 * 18, inventoryHotbarY));
        }

        // Shipyard blueprint item slots
        int bpX = 26;
        int bpY = 12;
        for (int i = 0; i < this.shipyardBlockEntity.processShipSize; i++) {
            var blueprintSlot = new BlueprintSlot(this.shipyardContainer, i, i, bpX, bpY + 23 * i, this.shipyardBlockEntity.getBlockPos());
            this.addSlot(blueprintSlot);
            this.blueprintSlots.add(blueprintSlot);
        }

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
                if (!this.moveItemStackTo(itemstack1, 36, 36 + this.shipyardBlockEntity.processShipSize, false)) {
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
        return AbstractContainerMenu.stillValid(this.access, pPlayer, ModBlock.SHIPYARD.get());
    }

    public ShipyardBlockEntity getShipyardBlockEntity() {
        return shipyardBlockEntity;
    }


}
