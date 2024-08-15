package com.github.icecheesecat.shincolle.datagen;

import com.github.icecheesecat.shincolle.Shincolle;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shincolle.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        ExistingFileHelper efh = event.getExistingFileHelper();

        gen.addProvider(event.includeClient(), new ModItemModelProvider(output, Shincolle.MODID, efh));
//        gen.addProvider(
//                event.includeClient(),
//                output -> new MyBlockStateProvider(output, MOD_ID, efh)
//        );
    }

}
