package com.github.icecheesecat.kantaicraft.block.shipyardUtil;

import com.github.icecheesecat.kantaicraft.item.ShipBlueprintData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class BlueprintCell implements INBTSerializable<CompoundTag> {

    private int progress;
    private int maxProgress;
    private ItemStack blueprint;
    private ShipBlueprintData data;
    private UUID builder;

    public BlueprintCell() {
        this.blueprint = ItemStack.EMPTY;
        this.builder = UUID.randomUUID();
        this.data = ShipBlueprintData.empty();
        this.progress = this.maxProgress = -1;
    }

    public BlueprintCell(CompoundTag nbt) {
        this();
        this.deserializeNBT(nbt);
    }

    public ItemStack getBlueprint() {
        return blueprint;
    }

    public void setNewBlueprint(ItemStack blueprint, UUID builder)
    {
        if (blueprint == ItemStack.EMPTY) {
            this.setEmpty();
        }
        else {
            this.blueprint = blueprint;
            this.data = ShipBlueprintData.read(blueprint.getTag());
            this.progress = 0;
            this.maxProgress = this.data.getProcessTime();
            this.builder = builder;
        }
    }

    public void setEmpty() {
        this.blueprint = ItemStack.EMPTY;
        this.maxProgress = -1;
        this.progress = -1;
        this.data = ShipBlueprintData.empty();
        this.builder = UUID.randomUUID();
    }

    public UUID getBuilder() {
        return builder;
    }

    public ShipBlueprintData getData() {
        return data;
    }

    public ItemStack removeBlueprint(boolean consume) {
        ItemStack ret = this.blueprint;
        this.setEmpty();

        return consume ? ItemStack.EMPTY : ret;
    }

    public void doProgress() {
        if (isEmpty()) return;
        this.progress++;
    }

    public boolean isEmpty() {
        return this.blueprint.isEmpty();
    }

    public boolean done() {
        return this.progress > maxProgress;
    }

    public boolean tick() {
        if (this.isEmpty()) return false;
        this.doProgress();
        return true;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("progress", this.progress);
        nbt.putInt("max_progress", this.maxProgress);
        nbt.put("blueprint", this.blueprint.serializeNBT());
        nbt.putUUID("builder", this.builder);
        nbt.put("shipblueprintdata", this.data.write());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.progress = nbt.getInt("progress");
        this.maxProgress = nbt.getInt("max_progress");
        this.blueprint = ItemStack.of(nbt.getCompound("blueprint"));
        this.builder = nbt.getUUID("builder");
        this.data = ShipBlueprintData.read(nbt.getCompound("shipblueprintdata"));
    }

    public String remainTime() {
        if (this.done()) {
            return "DONE";
        }

        int tick = this.maxProgress - this.progress;
        int hour = tick / 60 / 60 / 20;
        int minute = tick / 60 / 20 - hour * 60;
        int second = tick / 20 - minute * 60 - hour * 3600;

        String ret = "";
        if (hour > 0) {
            ret = hour + " hrs " + minute + " min " + second + " sec";
        }
        else {
            if (minute > 0) {
                ret = minute + " min " + second + " sec";
            }
            else {
                ret = second + " sec";
            }
        }

        return ret;
    }
}
