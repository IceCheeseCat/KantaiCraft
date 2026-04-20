package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.item.BlueprintItem;
import com.github.icecheesecat.kantaicraft.item.ShipSpawnEgg;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class ModItem
{

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KantaiCraft.MODID);
	public static final List<RegistryObject<ShipSpawnEgg>> SPAWN_EGGS = new ArrayList<>();

	public static final RegistryObject<ShipSpawnEgg> INAZUMA_SPAWN_EGG = registerEntitySpawnEgg("inazuma", ModEntity.INAZUMA, 0x7d4100, 0x945512);
	public static final RegistryObject<ShipSpawnEgg> IKAZUCHI_SPAWN_EGG = registerEntitySpawnEgg("ikazuchi", ModEntity.IKAZUCHI, 0xc24100, 0xe66220);
	public static final RegistryObject<ShipSpawnEgg> HIBIKI_SPAWN_EGG = registerEntitySpawnEgg("hibiki", ModEntity.HIBIKI, 0x98cfed, 0xbce2f7);
	public static final RegistryObject<ShipSpawnEgg> AKATSUKI_SPAWN_EGG = registerEntitySpawnEgg("akatsuki", ModEntity.AKATSUKI, 0x130038, 0x270a5e);
	public static final RegistryObject<Item> SHIP_BLUEPRINT = ITEMS.register("ship_blueprint", () -> new BlueprintItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> AMMO = ITEMS.register("ammo", () -> new Item(new Item.Properties()));

	private static RegistryObject<ShipSpawnEgg> registerEntitySpawnEgg(String name, RegistryObject<? extends EntityType<? extends Mob>> entityTypeRegistryObject, int background, int highlight) {
		var ret = ITEMS.register(name + "_spawn_egg", () -> new ShipSpawnEgg(entityTypeRegistryObject, background, highlight,
				(ship, player) -> {
					ship.setShipOwner(player.getUUID());
					ship.addFuel(new FluidStack(Fluids.LAVA, 64000), IFluidHandler.FluidAction.EXECUTE);
				}
				, new Item.Properties()));
		SPAWN_EGGS.add(ret);
		return ret;
	}
}