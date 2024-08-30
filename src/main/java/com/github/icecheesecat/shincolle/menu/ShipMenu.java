package com.github.icecheesecat.shincolle.menu;

import com.github.icecheesecat.shincolle.entity.BasicEntityShip;
import com.github.icecheesecat.shincolle.init.ModMenus;
import com.github.icecheesecat.shincolle.menu.screen.ShipScreen;
import com.github.icecheesecat.shincolle.util.EquipmentSlots;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.IContainerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShipMenu extends AbstractContainerMenu implements IContainerFactory<ShipMenu> {

    final Inventory inventory;
    EquipmentSlots equipmentSlots;
    @Nullable BasicEntityShip entityShip;


    // server
    public ShipMenu(int containerId, Inventory inv, @Nullable BasicEntityShip entityShip, EquipmentSlots equipmentSlots) {
        super(ModMenus.SHIP_MENU.get(), containerId);

        this.inventory = inv;
        this.equipmentSlots = equipmentSlots;
        this.entityShip = entityShip;

    }

    // client
    public ShipMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        this(containerId, inv, null, EquipmentSlots.readBuffer(buf));
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
}
