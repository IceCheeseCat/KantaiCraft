package com.github.icecheesecat.kantaicraft.datagen;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    private static final List<Block> FACILITIES = List.of(
            ModBlock.FACILITY.get(),
            ModBlock.SHIPYARD.get(),
            ModBlock.FUEL_STATION.get(),
            ModBlock.COMMAND_CENTER.get());

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, KantaiCraft.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.makePickaxeMineableBlock(asBlockArray(FACILITIES));
        this.makeStoneMineableBlock(asBlockArray(FACILITIES));
    }

    private void makeStoneMineableBlock(Block... blocks) {
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(blocks);
    }

    private void makePickaxeMineableBlock(Block... blocks) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blocks);
    }

    private Block[] asBlockArray(List<Block> blocks) {

        Block[] ba = new Block[blocks.size()];
        for (int i = 0; i < blocks.size(); i++) {
            ba[i] = blocks.get(i);
        }

        return ba;
    }

}
