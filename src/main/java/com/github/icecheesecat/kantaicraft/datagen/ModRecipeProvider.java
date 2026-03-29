package com.github.icecheesecat.kantaicraft.datagen;

import com.github.icecheesecat.kantaicraft.registries.ModItem;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ammoRecipeBuilder(pWriter, ModItem.AMMO.get(), Items.IRON_INGOT, Items.GUNPOWDER);
    }

    public static void ammoRecipeBuilder(Consumer<FinishedRecipe> finishedRecipeConsumer, ItemLike ammo, ItemLike item1, ItemLike gunpowder) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ammo, 16)
                .define('#', item1)
                .define('G', gunpowder)
                .pattern(" # ")
                .pattern("#G#")
                .pattern(" # ")
                .unlockedBy(getHasName(ammo), has(ammo))
                .save(finishedRecipeConsumer);
    }
}
