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
			}
	, new Item.Properties()));


	public static final RegistryObject<Item> SHIP_BLUEPRINT = ITEMS.register("ship_blueprint", () -> new BlueprintItem(new Item.Properties().stacksTo(1)));

	//	//spawn egg
//	public static BasicItem ShipSpawnEgg;
//	//materials

//	//equip
//	public static BasicItem EquipCannon;
//	public static BasicItem EquipCatapult;
//	public static BasicItem EquipCompass;
//	public static BasicItem EquipDrum;
//	public static BasicItem EquipFlare;
//	public static BasicItem EquipMachinegun;
//	public static BasicItem EquipRadar;
//	public static BasicItem EquipSearchlight;
//	public static BasicItem EquipTorpedo;
//	public static BasicItem EquipTurbine;
//	//misc
//	public static BasicItem BucketRepair;
//	public static BasicItem CombatRation;
//	public static BasicItem DeskItemBook;
//	public static BasicItem DeskItemRadar;
//	public static BasicItem InstantConMat;
//	public static BasicItem KaitaiHammer;
//	public static BasicItem MarriageRing;
//	public static BasicItem ModernKit;
//	public static BasicItem OwnerPaper;
//	public static BasicItem OPTool;
//	public static BasicItem PointerItem;
//	public static BasicItem RecipePaper;
//	public static BasicItem RepairGoddess;
//	public static BasicItem ShipTank;
//	public static BasicItem TargetWrench;
//	public static BasicItem TrainingBook;
//	//toy
//	public static BasicItem ToyAirplane;
//
//	//list for items
//	private static List<BasicItem> ListItems;
//
//
//	//item instance init, used in MOD PREINIT
//	public static void init() throws Exception
//	{
//		ListItems = new ArrayList();
//
//		//spawn egg
//		ShipSpawnEgg = initItems(ShipSpawnEgg.class);
//
//		//materials
//		AbyssMetal = initItems(AbyssMetal.class);
//		AbyssNugget = initItems(AbyssNugget.class);
//		Ammo = initItems(Ammo.class);
//		Grudge = initItems(Grudge.class);
//
//		//equip
//		EquipAirplane = initItems(EquipAirplane.class);
//		EquipAmmo = initItems(EquipAmmo.class);
//		EquipArmor = initItems(EquipArmor.class);
//		EquipCannon = initItems(EquipCannon.class);
//		EquipCatapult = initItems(EquipCatapult.class);
//		EquipCompass = initItems(EquipCompass.class);
//		EquipDrum = initItems(EquipDrum.class);
//		EquipFlare = initItems(EquipFlare.class);
//		EquipMachinegun = initItems(EquipMachinegun.class);
//		EquipRadar = initItems(EquipRadar.class);
//		EquipSearchlight = initItems(EquipSearchlight.class);
//		EquipTorpedo = initItems(EquipTorpedo.class);
//		EquipTurbine = initItems(EquipTurbine.class);
//
//		//misc
//		BucketRepair = initItems(BucketRepair.class);
//		CombatRation = initItems(CombatRation.class);
//		DeskItemBook = initItems(DeskItemBook.class);
//		DeskItemRadar = initItems(DeskItemRadar.class);
//		InstantConMat = initItems(InstantConMat.class);
//		KaitaiHammer = initItems(KaitaiHammer.class);
//		MarriageRing = initItems(MarriageRing.class);
//		ModernKit = initItems(ModernKit.class);
//		OwnerPaper = initItems(OwnerPaper.class);
//		OPTool = initItems(OPTool.class);
//		PointerItem = initItems(PointerItem.class);
//		RecipePaper = initItems(RecipePaper.class);
//		RepairGoddess = initItems(RepairGoddess.class);
//		ShipTank = initItems(ShipTank.class);
//		TargetWrench = initItems(TargetWrench.class);
//		TrainingBook = initItems(TrainingBook.class);
//
//		//toy
//		ToyAirplane = initItems(ToyAirplane.class);
//
//	}
//
//	//create instance and add instance to list
//	private static BasicItem initItems(Class<? extends BasicItem> itemClass) throws Exception
//	{
//		try
//		{
//			BasicItem i = itemClass.newInstance();
//			ListItems.add(i);
//			return i;
//		}
//		catch (Exception e)
//		{
//			//item建立instance失敗, 此例外必須丟出以強制中止遊戲
//			LogHelper.info("EXCEPTION: instancing fail: "+itemClass);
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	//item model init, used in CLIENT PROXY INIT
//	@SideOnly(Side.CLIENT)
//    public static void initModels()
//	{
//		for (BasicItem i : ListItems)
//		{
//			i.initModel();
//		}
//    }
	
	
}