package com.github.icecheesecat.kantaicraft.common;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.equipment.cannon.Cannon;
import com.github.icecheesecat.kantaicraft.equipment.cannon.CannonStats;
import com.github.icecheesecat.kantaicraft.init.ModShipAttributes;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class Equipments {

    public static final Cannon TwelveCMSmallGunMount = new Cannon(101, "12cm_small_gun_mount", new CannonStats(0).addStats(ModShipAttributes.FIREPOWER.get(), 1.0d), 2*20, 100.0f, 20.0f);


}
