package com.github.icecheesecat.kantaicraft.container;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class ShipContainer extends SimpleContainer {

    BasicEntityShip ship;
    private final int upgradeLevel = 10;
    private final int upgradeSize = 9;

    public ShipContainer(BasicEntityShip ship, int pSize) {
        super(pSize);
        this.ship = ship;
    }

    @Override
    public boolean canAddItem(ItemStack pStack) {
        if (this.ship.level())
        return super.canAddItem(pStack);
    }
}
