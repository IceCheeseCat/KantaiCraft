package com.github.icecheesecat.kantaicraft.entity;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.world.entity.LivingEntity;

public interface IFaction<T> {

    int getFactionId();

    void setFactionId(int factionId);

    boolean isEnemy(LivingEntity livingEntity);

}
