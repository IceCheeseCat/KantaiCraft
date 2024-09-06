package com.github.icecheesecat.kantaicraft.menu;

import ca.weblite.objc.Client;
import com.github.icecheesecat.kantaicraft.entity.BasicEntityShip;
import com.github.icecheesecat.kantaicraft.init.ModMenus;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentSlots;
import com.github.icecheesecat.kantaicraft.stats.ShipStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.IContainerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public class ShipMenu extends AbstractContainerMenu implements IContainerFactory<ShipMenu> {

    final Inventory inventory;
    EquipmentSlots equipmentSlots;
    @NotNull BasicEntityShip entityShip;


    // server
    public ShipMenu(int containerId, Inventory inv, @Nullable BasicEntityShip entityShip, EquipmentSlots equipmentSlots) {
        super(ModMenus.SHIP_MENU.get(), containerId);

        this.inventory = inv;
        this.equipmentSlots = equipmentSlots;
        this.entityShip = entityShip;

    }

    // client
    public ShipMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        super(ModMenus.SHIP_MENU.get(), containerId);
        int id = buf.readInt();
        int slotSize = buf.readByte();
        this.inventory = inv;
        this.equipmentSlots = EquipmentSlots.readBuffer(slotSize, ByteBuffer.wrap(buf.readByteArray()));

        ClientLevel c_level = Minecraft.getInstance().level;
        if (c_level == null) {
            throw new IllegalStateException(String.format("Minecraft instance level is null at %s", this.getClass().getCanonicalName()));
        }
        if (c_level.getEntity(id) instanceof BasicEntityShip entityShip) {
            this.entityShip = entityShip;
        }
        else {
            throw new IllegalStateException(String.format("Failed to correctly get corresponding entity.getId at %s", this.getClass().getCanonicalName()));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

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

    public Inventory getInventory() {
        return inventory;
    }

    public EquipmentSlots getEquipmentSlots() {
        return equipmentSlots;
    }

    public @Nullable BasicEntityShip getEntityShip() {
        return entityShip;
    }

}
