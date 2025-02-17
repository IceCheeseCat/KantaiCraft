package com.github.icecheesecat.kantaicraft.block.shipyardUtil;

import com.github.icecheesecat.kantaicraft.item.ShipBlueprintData;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class BuiltData implements INBTSerializable<CompoundTag> {

    private ShipBlueprintData data;
    private UUID uuid;

    public BuiltData(ShipBlueprintData data) {
        this.data = data;
        this.uuid = UUID.randomUUID();
    }

    public static BuiltData read(CompoundTag nbt) {
        BuiltData builtData = new BuiltData(null);
        builtData.deserializeNBT(nbt);

        return builtData;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ShipBlueprintData getData() {
        return data;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("data", data.write());
        nbt.putUUID("uuid", this.uuid);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.data = ShipBlueprintData.read(nbt.getCompound("data"));
        this.uuid = nbt.getUUID("uuid");
    }
}
