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
        this.simpleBlockWithItem(ModBlock.FLOOR.get(), cubeAll(ModBlock.FLOOR.get()));
        this.simpleBlockWithItem(ModBlock.CRANE.get(), cubeAll(ModBlock.CRANE.get()));
        this.simpleBlockWithItem(ModBlock.SHIPYARD_CORE.get(), cubeAll(ModBlock.SHIPYARD_CORE.get()));
        this.simpleBlockWithItem(ModBlock.COMMAND_CENTER.get(), cubeAll(ModBlock.COMMAND_CENTER.get()));
    }
}
