package com.github.icecheesecat.shincolle.datagen;

import com.github.icecheesecat.shincolle.Shincolle;
import com.github.icecheesecat.shincolle.init.ModItems;
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
//        simpleItem(ModItems.TwelveCMSmallGunMount);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        System.out.println(item.getId().getPath());
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                    new ResourceLocation(Shincolle.MODID, "item/" + item.getId().getPath()));
    }
}
