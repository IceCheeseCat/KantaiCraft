package com.github.icecheesecat.kantaicraft.entity;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;

public interface IFaction<T> {

    int getFactionId();

    void setFactionId(int factionId);

    boolean isEnemy(T other);

}
