package com.github.icecheesecat.shincolle.entity.util;

import com.github.icecheesecat.shincolle.entity.util.attack.ShipCanonAttack;
import com.github.icecheesecat.shincolle.entity.util.attack.ShipMeleeAttack;
import com.github.icecheesecat.shincolle.weaponary.canon.Canon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicCanonShip extends BasicEntityShip {

    protected ShipMeleeAttack shipMeleeAttack;
    protected List<ShipCanonAttack> shipCanonAttack_list;

    protected BasicCanonShip(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.shipMeleeAttack = new ShipMeleeAttack(this);
        shipCanonAttack_list = new ArrayList<>();
    }

    abstract protected boolean addCanon(Canon c);
    public List<ShipCanonAttack> getShipCanonAttack() {
        return shipCanonAttack_list;
    }

}
