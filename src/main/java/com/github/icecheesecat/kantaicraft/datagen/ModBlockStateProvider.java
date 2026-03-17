package com.github.icecheesecat.kantaicraft.datagen;

import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockWithItem(ModBlock.COMMAND_CENTER.get(), cubeAll(ModBlock.COMMAND_CENTER.get()));
        this.simpleBlockWithItem(ModBlock.FACILITY.get(), cubeAll(ModBlock.FACILITY.get()));
        this.simpleBlockWithItem(ModBlock.SHIPYARD.get(), cubeAll(ModBlock.SHIPYARD.get()));
        this.simpleBlockWithItem(ModBlock.FUEL_STATION.get(), cubeAll(ModBlock.FUEL_STATION.get()));
    }
}
