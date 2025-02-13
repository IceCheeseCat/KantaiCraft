package com.github.icecheesecat.kantaicraft.block.shipyardUtil;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BuildSlotContainer implements Container {

    NonNullList<BlueprintSlot> blueprintSlots;

    public BuildSlotContainer(NonNullList<BlueprintSlot> blueprintSlots) {
        this.blueprintSlots = blueprintSlots;
    }

    @Override
    public int getContainerSize() {
        return blueprintSlots.size();
    }

    @Override
    public boolean isEmpty() {
        return this.blueprintSlots.stream().allMatch(BlueprintSlot::isEmpty);
    }

    public int hasEmptySlotAt() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (this.blueprintSlots.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    public boolean canInsertAt(int i, ItemStack stack) {
        return this.blueprintSlots.get(i).canInsertBluePrint(stack);
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return this.blueprintSlots.get(pSlot).getBlueprint();
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack ret = this.blueprintSlots.get(pSlot).removeBlueprint(false);
        if (!ret.isEmpty()) {
            this.setChanged();
        }

        return ret;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return this.blueprintSlots.get(pSlot).removeBlueprint(false);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.blueprintSlots.get(pSlot).setBlueprint(pStack);
        this.setChanged();
    }

    @Override
    public void setChanged() {
        // idk what to do :D
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        this.blueprintSlots.clear();
        this.setChanged();
    }
}
