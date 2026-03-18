package com.github.icecheesecat.kantaicraft.datagen;

import com.github.icecheesecat.kantaicraft.block.TwoPart;
import com.github.icecheesecat.kantaicraft.block.TwoPartBlock;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTableSubProvider extends BlockLootSubProvider {

    protected ModBlockLootTableSubProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlock.FACILITY.get());
        this.dropSelf(ModBlock.FUEL_STATION.get());
        this.dropSelf(ModBlock.SHIPYARD.get());
        this.dropTwoPartBlock((TwoPartBlock) ModBlock.COMMAND_CENTER.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlock.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    protected void dropTwoPartBlock(TwoPartBlock block) {
        LootItemCondition.Builder lootItemCondition = new LootItemBlockStatePropertyCondition.Builder(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TwoPartBlock.TWO_PART, TwoPart.Front));
        LootTable.Builder lootTable = LootTable.lootTable().withPool(
                this.applyExplosionCondition(block,
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).when(lootItemCondition))));
        this.add(block, lootTable);
    }

    public LootTable.Builder createSingleItemTable(ItemLike pItem) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(pItem, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(pItem))));
    }
}
