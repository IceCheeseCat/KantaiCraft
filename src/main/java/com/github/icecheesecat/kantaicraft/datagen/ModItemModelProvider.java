package com.github.icecheesecat.kantaicraft.datagen;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
//        this.modelName = modelName;
    }

    @Override
    protected void registerModels() {
//        simpleItem(ModItem.TwelveCMSmallGunMount);
        simpleItem(ModItem.INAZUMA_SPAWN_EGG);
        simpleItem(ModItem.HOSTILE_INAZUMA_SPAWN_EGG);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                    new ResourceLocation(KantaiCraft.MODID, "item/" + item.getId().getPath()));
    }
}
