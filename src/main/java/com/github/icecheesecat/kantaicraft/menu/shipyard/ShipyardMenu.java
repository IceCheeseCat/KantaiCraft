package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BlueprintSlot;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuildSlotContainer;
import com.github.icecheesecat.kantaicraft.block.shipyardUtil.BuiltShip;
import com.github.icecheesecat.kantaicraft.item.ShipBlueprintData;
import com.github.icecheesecat.kantaicraft.menu.DynamicContainerData;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShipyardMenu extends AbstractContainerMenu {

    private NonNullList<BlueprintSlot> blueprintSlots;
    private ContainerData builtData;
    private List<BuiltShip> builtShips;
    private BuildSlotContainer buildSlotContainer;
    private Inventory inventory;

    // server side
    public ShipyardMenu(int pContainerId, Inventory inventory, NonNullList<BlueprintSlot> blueprintSlots, List<BuiltShip> builtShips) {
        super(ModMenu.SHIPYARD_MENU.get(), pContainerId);
        this.blueprintSlots = blueprintSlots;
        this.buildSlotContainer = new BuildSlotContainer(this.blueprintSlots);
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
        for (int slotIndex = 0; slotIndex < this.blueprintSlots.size(); slotIndex++) {
            this.addSlot(new Slot(this.buildSlotContainer, slotIndex, 10 + slotIndex * 18, 20));
        }

        this.builtShips = builtShips;

        // container data for builtslots
        this.builtData = new DynamicContainerData();
        this.addDataSlots(this.builtData);

    }

    // client side
    public ShipyardMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, readBuildSlots(extraData), readBuiltShip(extraData));
    }

    private static List<BuiltShip> readBuiltShip(FriendlyByteBuf extraData) {
        byte size = extraData.readByte();
        List<BuiltShip> nList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            nList.add(i, new BuiltShip(ShipBlueprintData.read(extraData.readNbt())));
        }

        return nList;
    }

    private static NonNullList<BlueprintSlot> readBuildSlots(FriendlyByteBuf extraData) {
        var nList = NonNullList.withSize(4, new BlueprintSlot());
        for (var slot: nList) {
            slot.deserializeNBT(extraData.readNbt());
        }

        return nList;
    }

    public void tickBuildSlots() {
        for (var slot: this.blueprintSlots) {
            if (slot.done()) {
                slot.removeBlueprint(true);
            }
            slot.doProgress();
        }
    }

    @Override
    public void slotsChanged(Container pContainer) {


    }



    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot clickedSlot = this.slots.get(pIndex);
        ItemStack clickedItemStack = clickedSlot.getItem();
        if (!clickedItemStack.isEmpty()) {
            return clickedItemStack;
        }

        if (clickedSlot.container instanceof Inventory inventory) {
            int firstEmpty = this.buildSlotContainer.hasEmptySlotAt();
            if (firstEmpty == -1) return clickedItemStack;
            this.buildSlotContainer.setItem(firstEmpty, clickedItemStack);
        }
        else if (clickedSlot.container instanceof BuildSlotContainer bsc) {
            int freeSlot = this.inventory.getFreeSlot();
            if (freeSlot == -1) return clickedItemStack;
            this.inventory.setItem(freeSlot, clickedItemStack);
        }

        return clickedItemStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.create(pPlayer.level(), pPlayer.blockPosition()), pPlayer, ModBlock.SHIPYARD.get());
    }
}
