package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.item.BlueprintItem;
import com.github.icecheesecat.kantaicraft.item.ShipSpawnEgg;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItem
{

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KantaiCraft.MODID);

	public static final RegistryObject<Item> INAZUMA_SPAWN_EGG = ITEMS.register("inazuma_with_full_resources", () -> new ShipSpawnEgg(ModEntity.PlayerShip.INAZUMA, FastColor.ABGR32.red(128), FastColor.ABGR32.green(128),
			(ship, player) -> {
				ship.setShipOwner(player.getUUID());
				ship.addFuel(new FluidStack(Fluids.LAVA, 64000), IFluidHandler.FluidAction.EXECUTE);
				ship.setAmmo(100.0f);
			}
	, new Item.Properties()));

	public static final RegistryObject<Item> INAZUMA_SPAWN_EGG_2 = ITEMS.register("inazuma_with_no_fuel", () -> new ShipSpawnEgg(ModEntity.PlayerShip.INAZUMA, FastColor.ABGR32.red(128), FastColor.ABGR32.green(128),
			(ship, player) -> {
				ship.setShipOwner(player.getUUID());
				ship.addFuel(new FluidStack(Fluids.LAVA, 30), IFluidHandler.FluidAction.EXECUTE);
				ship.setAmmo(100.0f);
			}
	, new Item.Properties()));

	public static final RegistryObject<Item> AKATSUKI_SPAWN_EGG = ITEMS.register("akatsuki_with_no_fuel", () -> new ShipSpawnEgg(ModEntity.PlayerShip.AKATSUKI, FastColor.ABGR32.red(128), FastColor.ABGR32.green(128),
			(ship, player) -> {
				ship.setShipOwner(player.getUUID());
				ship.setAmmo(100.0f);
			}
	, new Item.Properties()));

	public static final RegistryObject<Item> HOSTILE_INAZUMA_SPAWN_EGG = ITEMS.register("hostile_inazuma", () -> new ShipSpawnEgg(ModEntity.HostileShip.INAZUMA, FastColor.ABGR32.red(128), FastColor.ABGR32.green(128),
			(ship, player) -> {
				ship.setHealth(8.0f);
			}
	, new Item.Properties()));
	public static final RegistryObject<Item> SHIP_BLUEPRINT = ITEMS.register("ship_blueprint", () -> new BlueprintItem(new Item.Properties().stacksTo(1)));
	
}