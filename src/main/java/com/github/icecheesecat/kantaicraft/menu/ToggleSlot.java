package com.github.icecheesecat.kantaicraft.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class ToggleSlot extends Slot {
    boolean isActive = false;
    public ToggleSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
