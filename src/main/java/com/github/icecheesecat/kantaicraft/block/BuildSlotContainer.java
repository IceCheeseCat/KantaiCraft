package com.github.icecheesecat.kantaicraft.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class BuildSlotContainer implements Container {

    NonNullList<BuildSlot> buildSlots;

    public BuildSlotContainer(NonNullList<BuildSlot> buildSlots) {
        this.buildSlots = buildSlots;
    }

    @Override
    public int getContainerSize() {
        return buildSlots.size();
    }

    @Override
    public boolean isEmpty() {
        return this.buildSlots.stream().allMatch(BuildSlot::isEmpty);
    }

    public int hasEmptySlotAt() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (this.buildSlots.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    public boolean canInsertAt(int i, ItemStack stack) {
        return this.buildSlots.get(i).canInsertBluePrint(stack);
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return this.buildSlots.get(pSlot).getBlueprint();
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack ret = this.buildSlots.get(pSlot).removeBlueprint();
        if (!ret.isEmpty()) {
            this.setChanged();
        }

        return ret;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return this.buildSlots.get(pSlot).removeBlueprint();
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.buildSlots.get(pSlot).setBlueprint(pStack);
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
        this.buildSlots.clear();
        this.setChanged();
    }
}
