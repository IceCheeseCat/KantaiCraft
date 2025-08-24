package com.github.icecheesecat.kantaicraft.menu.ship;

import com.github.icecheesecat.kantaicraft.container.ShipContainer;
import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.menu.ToggleSlot;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
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
    final ShipContainer shipInventory;
    @NotNull BasicEntityShip entityShip;

    // server
    public ShipMenu(int containerId, Inventory inv, @NotNull BasicEntityShip entityShip) {
        super(ModMenu.SHIP_MENU.get(), containerId);

        this.playerInventory = inv;
        this.entityShip = entityShip;
        this.shipInventory = entityShip.getShipInventory();


        // player inventory
        int playerX = -42;
        int playerY = 132;
        int slotSize = 17;
        for (int i = 0; i < 9; i++) {
            for (int j = 1; j < 4; j++) {
                this.addSlot(new ToggleSlot(this.playerInventory,   i + j * 9, i * slotSize + playerX, playerY - j * slotSize - 4));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new ToggleSlot(this.playerInventory,   i, i * slotSize + playerX,  playerY));
        }

        // ship inventory
        int shipX = 146;
        int shipY = 132;
        for (int i = 0; i < 9; i++) {
            for (int j = 1; j < 4; j++) {
                this.addSlot(new ToggleSlot(this.shipInventory,   i + j * 9, i * slotSize + shipX, shipY - j * slotSize - 4));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new ToggleSlot(this.shipInventory,   i, i * slotSize + shipX,  shipY));
        }
    }

    // client
    public ShipMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        this(containerId, inv, (BasicEntityShip) Minecraft.getInstance().level.getEntity(buf.readInt()));
    }

    // TODO
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
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

    public @Nullable BasicEntityShip getEntityShip() {
        return entityShip;
    }

}
