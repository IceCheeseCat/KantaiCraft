package com.github.icecheesecat.kantaicraft.capability.indicateditementities;

import com.github.icecheesecat.kantaicraft.capability.LevelTickable;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class IndicatedItemEntities implements LevelTickable {

    final List<Integer> itemEntities;

    public IndicatedItemEntities() {
        itemEntities = new ArrayList<>();
    }

    @Override
    public void doTick() {
        itemEntities.removeIf(id -> {
            if (Minecraft.getInstance().level.getEntity(id) == null) return true;
            else return Minecraft.getInstance().level.getEntity(id).isRemoved();
        });
    }

    public void addNewInstance(int id) {
        itemEntities.add(id);
    }

    public List<Integer> getItemEntities() {
        return itemEntities;
    }
}
