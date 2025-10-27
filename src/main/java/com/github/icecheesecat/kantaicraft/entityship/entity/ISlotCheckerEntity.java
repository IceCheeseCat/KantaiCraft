package com.github.icecheesecat.kantaicraft.entityship.entity;

import com.github.icecheesecat.kantaicraft.equipment.SlotChecker;


/**
 * Equipment Handler uses SlotCheck to judge valid equipment for the BasicEntityShip. Override to
 * let entity hold other types of equipment.
 */
public interface ISlotCheckerEntity {

    SlotChecker getSlotChecker(int index);

}
