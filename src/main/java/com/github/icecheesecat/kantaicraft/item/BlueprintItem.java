package com.github.icecheesecat.kantaicraft.item;

import com.github.icecheesecat.kantaicraft.blueprint.Blueprint;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class BlueprintItem extends Item {

    public BlueprintItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        var blueprint = Blueprint.createFromTag(pStack.getTag());

        return blueprint.getRarity();
    }

}
