package com.github.icecheesecat.kantaicraft.block.shipyardUtil;

import com.github.icecheesecat.kantaicraft.item.BlueprintItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

public class BlueprintSlot implements INBTSerializable<CompoundTag> {

    private int progress;
    private int maxProgress;
    private ItemStack blueprint;

    public BlueprintSlot() {
        blueprint = ItemStack.EMPTY;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public ItemStack getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(ItemStack blueprint) {
        this.blueprint = blueprint;
    }

    public void insertBlueprint(ItemStack stack) {
        this.blueprint = stack;
        this.progress = 0;
        this.maxProgress = ((BlueprintItem) stack.getItem()).getMaxProgress(stack);
    }

    public ItemStack removeBlueprint(boolean consume) {
        ItemStack ret = this.blueprint;
        this.blueprint = ItemStack.EMPTY;
        this.progress = 0;
        this.maxProgress = 0;

        return consume ? ItemStack.EMPTY : ret;
    }

    public boolean canInsertBluePrint(ItemStack stack) {
        return stack.getItem() instanceof BlueprintItem blueprintItem && blueprintItem.isValidStack(stack) && isEmpty();
    }

    public void doProgress() {
        if (isEmpty()) return;
        this.progress++;
    }

    public boolean isEmpty() {
        return this.blueprint.isEmpty();
    }

    public boolean done() {
        return this.progress >= maxProgress;
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("progress", this.progress);
        nbt.putInt("max_progress", this.maxProgress);
        nbt.put("blueprint", this.blueprint.serializeNBT());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.progress = nbt.getInt("progress");
        this.maxProgress = nbt.getInt("max_progress");
        this.blueprint = ItemStack.of(nbt.getCompound("blueprint"));
    }
}
