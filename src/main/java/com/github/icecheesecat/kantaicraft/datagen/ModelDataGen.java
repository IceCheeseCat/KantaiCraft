package com.github.icecheesecat.kantaicraft.datagen;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        ExistingFileHelper efh = event.getExistingFileHelper();

        gen.addProvider(event.includeClient(), new ModItemModelProvider(output, KantaiCraft.MODID, efh));
        gen.addProvider(event.includeClient(), new ModBlockStateProvider(output, KantaiCraft.MODID, efh));
//        gen.addProvider(event.includeServer(), new CannonDataProvider(output, KantaiCraft.MODID, EquipmentDataProvider.SMALL_CANNON_FOLDER,efh));

    }

}
