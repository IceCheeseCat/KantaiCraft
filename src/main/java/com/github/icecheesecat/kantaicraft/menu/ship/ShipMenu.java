package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.menu.ToggleSlot;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.IContainerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShipMenu extends AbstractContainerMenu implements IContainerFactory<ShipMenu> {

    final Inventory playerInventory;
    final Container shipInventory;
    @NotNull EntityShip entityShip;

    // server
    public ShipMenu(int containerId, Inventory inv, @NotNull EntityShip entityShip) {
        super(ModMenu.SHIP_MENU.get(), containerId);

        this.playerInventory = inv;
        this.entityShip = entityShip;
        this.shipInventory = entityShip.getShipInventory();

        // player inventory
        int playerX = - 154  - 19;
        int playerY = 0;
        int slotSize = 17;
        int playerHotbarY = playerY + slotSize * 3 + 8;
        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new ToggleSlot(this.playerInventory, j1 + (l + 1) * 9, playerX + j1 * slotSize, playerY + l * slotSize + 4));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new ToggleSlot(this.playerInventory, i1, playerX + i1 * slotSize, playerHotbarY));
        }



        // ship inventory
        int shipX = 19;
        int shipY = 0;
        int shipHotbarY = shipY + slotSize * 3 + 8;
        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new ToggleSlot(this.shipInventory, j1 + (l + 1) * 9, shipX + j1 * slotSize, shipY + l * slotSize + 4));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new ToggleSlot(this.shipInventory, i1, shipX + i1 * slotSize, shipHotbarY));
        }
    }

    // client
    public ShipMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        this(containerId, inv, (EntityShip) Minecraft.getInstance().level.getEntity(buf.readInt()));
    }

    // TODO
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        int containerRows = 4;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < containerRows * 9) {
                if (!this.moveItemStackTo(itemstack1, containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, containerRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    // TODO entity stop moving and look at player
    @Override
    public boolean stillValid(@NotNull Player player) {
        if (entityShip == null) return false;
        return player.distanceTo(entityShip) < 8.0d;
    }

    @Override
    public ShipMenu create(int windowId, Inventory inv, FriendlyByteBuf data) {
        return new ShipMenu(windowId, inv, data);
    }

    @Override
    public ShipMenu create(int p_create_1_, Inventory p_create_2_) {
        return IContainerFactory.super.create(p_create_1_, p_create_2_);
    }

    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    public @Nullable EntityShip getEntityShip() {
        return entityShip;
    }

}
