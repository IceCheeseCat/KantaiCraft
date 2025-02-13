package com.github.icecheesecat.kantaicraft.menu;

import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

import java.util.ArrayList;
import java.util.List;

public class DynamicContainerData implements ContainerData {
    private List<Integer> ints = new ArrayList<>();
    @Override
    public int get(int pIndex) {
        return ints.get(pIndex);
    }

    @Override
    public void set(int pIndex, int pValue) {
        if (ints.size() - 1 < pIndex) {
            for (int i = ints.size(); i < pIndex; i++) {
                ints.add(i, Integer.MIN_VALUE);
            }
            ints.add(pIndex, pValue);
        }
        else {
            ints.set(pIndex, pValue);
        }
    }

    @Override
    public int getCount() {
        return ints.size();
    }
}
