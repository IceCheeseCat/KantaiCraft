package com.github.icecheesecat.shincolle.equipment;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Set;

public class ResourceRefund {

    int count = 0;
    public Set<ItemStack> funds;

    public ResourceRefund addStack(ItemStack stack) {
        funds.add(stack);
        count++;
        return this;
    }

    public void addToInventory(Player player) {
        for (var f: funds) {
            player.getInventory().add(f);
        }
    }

    public static ResourceRefund get(int uid) {
        switch (uid) {
            case 101 -> {
                return TwelveCMSingleGunMount;
            }
            default -> {
                return new ResourceRefund();
            }
        }
    }

    public static final ResourceRefund TwelveCMSingleGunMount = new ResourceRefund().addStack(new ItemStack(Items.IRON_INGOT));

}
