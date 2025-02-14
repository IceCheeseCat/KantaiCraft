package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BlueprintCell;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuiltShipCell;
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
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ShipyardMenu extends AbstractContainerMenu {

    private NonNullList<BlueprintCell> blueprintCells;
    private List<BuiltShipCell> builtShipCells;
    private SlotItemHandler blueprintContainer;
    private Inventory inventory;
    private ContainerLevelAccess access;

    // server side
    public ShipyardMenu(int pContainerId, Inventory inventory, NonNullList<BlueprintCell> blueprintCells, List<BuiltShipCell> builtShipCells, ContainerLevelAccess access) {
        super(ModMenu.SHIPYARD_MENU.get(), pContainerId);
        this.blueprintCells = blueprintCells;
        this.blueprintContainer = new S

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

        // blueprint container
        for (int slotIndex = 0; slotIndex < this.blueprintCells.size(); slotIndex++) {
            this.addSlot(new Slot(this.blueprintContainer, slotIndex, 10 + slotIndex * 18, 20));
        }

        this.builtShipCells = builtShipCells;

        this.access = access;
    }

    // client side
    public ShipyardMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, readBlueprintCells(extraData), readBuiltShipCells(extraData), ContainerLevelAccess.NULL);
    }

    private static List<BuiltShipCell> readBuiltShipCells(FriendlyByteBuf extraData) {
        byte size = extraData.readByte();
        List<BuiltShipCell> nList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            nList.add(i, new BuiltShipCell(extraData.readNbt()));
        }

        return nList;
    }

    private static NonNullList<BlueprintCell> readBlueprintCells(FriendlyByteBuf extraData) {
        NonNullList<BlueprintCell> nList = NonNullList.create();
        for (int i = 0; i < 4; i++) {
            nList.add(i, new BlueprintCell(extraData.readNbt()));
        }

        return nList;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot clickedSlot = this.slots.get(pIndex);
        ItemStack clickedItemStack = clickedSlot.getItem();
        if (clickedItemStack.isEmpty()) return ItemStack.EMPTY;
        if (!clickedItemStack.is(ModItem.SHIP_BLUEPRINT.get())) return ItemStack.EMPTY;

        if (clickedSlot.container instanceof Inventory inventory) {
            ItemStack ret = this.blueprintContainer.addItem(clickedItemStack);
            return ret;
        }
        else if (clickedSlot.container instanceof SimpleContainer bsc) {

            if (!this.inventory.add(clickedItemStack)) {
                return clickedItemStack;
            }
            return ItemStack.EMPTY;
        }

        return clickedItemStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(this.access, pPlayer, ModBlock.SHIPYARD.get());
    }

    public SimpleContainer getBlueprintContainer() {
        return blueprintContainer;
    }

    public NonNullList<BlueprintCell> getBlueprintCells() {
        return blueprintCells;
    }
}
