package com.github.icecheesecat.kantaicraft.entity.ship.destroyer;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import com.github.icecheesecat.kantaicraft.entity.ship.DestroyerShip;
import com.github.icecheesecat.kantaicraft.entity.stance.EntityStance;
import com.github.icecheesecat.kantaicraft.entity.stance.HostileStance;
import com.github.icecheesecat.kantaicraft.entity.stance.NeutralStance;
import com.github.icecheesecat.kantaicraft.entity.stance.PlayerStance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class DestroyerIClass extends DestroyerShip {

    public DestroyerIClass(EntityType<? extends PathfinderMob> entityType, Level level, EntityStance entityStance) {
        super(entityType, level, entityStance);
    }

    @Override
    protected void initEquipments(EquipmentHandler equipmentHandler) {

    }

    @Override
    public int getProcessTime() {
        return 200;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

}
