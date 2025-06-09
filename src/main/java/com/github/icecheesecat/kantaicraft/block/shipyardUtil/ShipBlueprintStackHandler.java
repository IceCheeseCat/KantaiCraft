package com.github.icecheesecat.kantaicraft.block.shipyardUtil;

import com.github.icecheesecat.kantaicraft.capability.ShipBlueprintCapability;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ShipBlueprintStackHandler extends ItemStackHandler {

    byte[] dirty;

    public ShipBlueprintStackHandler(int size) {
        super(size);
        this.dirty = new byte[size];
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.is(ModItem.SHIP_BLUEPRINT.get())
                && stack.getCapability(ShipBlueprintCapability.TOKEN).isPresent();
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        this.dirty[slot] = 1;
    }

    public int getDirty() {
        for (int i = 0; i < this.getSlots(); i++) {
            if (dirty[i] == 1) {
                dirty[i] = 0;
                return i;
            }
        }

        return -1;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putByteArray("dirty", this.dirty);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.dirty = nbt.getByteArray("dirty");
    }
}
