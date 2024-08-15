//package com.github.icecheesecat.shincolle.init;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.github.icecheesecat.block.ICustomModels;
//import com.github.icecheesecat.utility.LogHelper;
//
//import net.minecraft.world.level.block.Block;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import net.minecraftforge.registries.RegistryObject;
//
//public class ModBlocks
//{
//	//new method
////	public static final RegistryObject<Block> ...
//
//	//blocks
////	public static Block BlockAbyssium;
////	public static Block BlockCrane;
////	public static Block BlockDesk;
////	public static Block BlockFrame;
////	public static Block BlockGrudge;
////	public static Block BlockGrudgeXP;
////	public static Block BlockGrudgeHeavy;
////	public static Block BlockGrudgeHeavyDeco;
////	public static Block BlockLightAir;
////	public static Block BlockLightLiquid;
////	public static Block BlockPolymetal;
////	public static Block BlockPolymetalGravel;
////	public static Block BlockPolymetalOre;
////	public static Block BlockSmallShipyard;
////	public static Block BlockVolBlock;
////	public static Block BlockVolCore;
////	public static Block BlockWaypoint;
//
//
//
//
////	public static void init() throws Exception
////	{
////		ListBlocks = new ArrayList();
////
////		BlockAbyssium = initBlocks(BlockAbyssium.class);
////		BlockCrane = initBlocks(BlockCrane.class);
////		BlockDesk = initBlocks(BlockDesk.class);
////		BlockFrame = initBlocks(BlockFrame.class);
////		BlockGrudge = initBlocks(BlockGrudge.class);
////		BlockGrudgeXP = initBlocks(BlockGrudgeXP.class);
////		BlockGrudgeHeavy = initBlocks(BlockGrudgeHeavy.class);
////		BlockGrudgeHeavyDeco = initBlocks(BlockGrudgeHeavyDeco.class);
////		BlockLightAir = initBlocks(BlockLightAir.class);
////		BlockLightLiquid = initBlocks(BlockLightLiquid.class);
////		BlockPolymetal = initBlocks(BlockPolymetal.class);
////		BlockPolymetalGravel = initBlocks(BlockPolymetalGravel.class);
////		BlockPolymetalOre = initBlocks(BlockPolymetalOre.class);
////		BlockSmallShipyard = initBlocks(BlockSmallShipyard.class);
////		BlockVolBlock = initBlocks(BlockVolBlock.class);
////		BlockVolCore = initBlocks(BlockVolCore.class);
////		BlockWaypoint = initBlocks(BlockWaypoint.class);
////
////	}
//
//	//create instance and add instance to list
////	private static Block initBlocks(Class<? extends Block> blockClass) throws Exception
////	{
////		try
////		{
////			Block i = blockClass.newInstance();
////			ListBlocks.add(i);
////			return i;
////		}
////		catch (Exception e)
////		{
////			//建立instance失敗, 此例外必須丟出以強制中止遊戲
////			LogHelper.info("EXCEPTION: block instancing fail: "+blockClass);
////			e.printStackTrace();
////			throw e;
////		}
////	}
//
//	//item model init, used in CLIENT PROXY INIT
////	@SideOnly(Side.CLIENT)
////    public static void initModels() throws Exception
////	{
////		for (Block b : ListBlocks)
////		{
////			try
////			{
////				((ICustomModels) b).initModel();
////			}
////			catch (Exception e)
////			{
////				//抓model失敗, 此例外必須丟出以強制中止遊戲
////				LogHelper.info("EXCEPTION: block texture init fail: "+b);
////				e.printStackTrace();
////				throw e;
////			}
////		}
////    }
//
//
//}