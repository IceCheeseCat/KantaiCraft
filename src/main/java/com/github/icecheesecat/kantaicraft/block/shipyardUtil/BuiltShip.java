package com.github.icecheesecat.kantaicraft.block.shipyardUtil;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.item.ShipBlueprintData;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class BuiltShip implements INBTSerializable<CompoundTag> {

    private ShipBlueprintData data;

    public BuiltShip(ShipBlueprintData data) {
        this.data = data;
    }

    public ResourceLocation getClassResource() {
        return new ResourceLocation(KantaiCraft.MODID, "textures/ship_class_icon/" + upperToUnderLine(data.shipClass().toString()) + ".png");
    }

    public ResourceLocation getResource() {
        return new ResourceLocation(KantaiCraft.MODID, "textures/ship_icon/" + upperToUnderLine(data.shipName().toString()) + ".png");
    }

    public ShipBlueprintData getData() {
        return data;
    }

    private String upperToUnderLine(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(str.charAt(0));
        for (int i = 1; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                sb.append("_");
            }
            sb.append(str.charAt(i));
        }

        String ret = sb.toString().toLowerCase();
        return ret;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("ship_blueprint_data", data.write());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.data = ShipBlueprintData.read(nbt.getCompound("ship_blueprint_data"));
    }
}
