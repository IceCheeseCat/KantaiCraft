package com.github.icecheesecat.kantaicraft.datagen;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator gen = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        ExistingFileHelper efh = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookUpProvider = event.getLookupProvider();

        gen.addProvider(event.includeClient(), new ModItemModelProvider(output, KantaiCraft.MODID, efh));
        gen.addProvider(event.includeClient(), new ModBlockStateProvider(output, KantaiCraft.MODID, efh));
        gen.addProvider(event.includeServer(), (DataProvider.Factory<LootTableProvider>) DataGenerator::createBlockLootTableProvider);
        gen.addProvider(event.includeServer(), new ModBlockTagsProvider(output, lookUpProvider, efh));
//        gen.addProvider(event.includeServer(), new CannonDataProvider(output, KantaiCraft.MODID, EquipmentDataProvider.SMALL_CANNON_FOLDER,efh));
        gen.addProvider(event.includeServer(), new ModRecipeProvider(output));
    }

    private static LootTableProvider createBlockLootTableProvider(PackOutput packOutput) {
        return new LootTableProvider(packOutput, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableSubProvider::new, LootContextParamSets.BLOCK)));
    }

}
