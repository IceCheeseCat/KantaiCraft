package com.github.icecheesecat.kantaicraft.init;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KantaiCraft.MODID);



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