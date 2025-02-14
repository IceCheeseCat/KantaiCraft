package com.github.icecheesecat.kantaicraft.menu.shipyard;

import com.github.icecheesecat.kantaicraft.item.BlueprintItem;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BlueprintSlot extends Slot {

    public BlueprintSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return pStack.is(ModItem.SHIP_BLUEPRINT.get()) && this.checkBlueprintData(pStack);
    }

    private boolean checkBlueprintData(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("ship_name") && stack.getTag().contains("ship_class");
    }

}
