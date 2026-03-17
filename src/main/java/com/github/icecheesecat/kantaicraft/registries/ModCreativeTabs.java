package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KantaiCraft.MODID);
    public static final RegistryObject<CreativeModeTab> TAB_0 = CREATIVE_MODE_TABS.register("tab_0", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.tab_0"))
            .icon(Items.ACACIA_DOOR::getDefaultInstance)
            .displayItems((parameters, output) -> {
                output.accept(ModItem.INAZUMA_SPAWN_EGG.get());
                output.accept(ModItem.INAZUMA_SPAWN_EGG_2.get());
                output.accept(ModItem.AKATSUKI_SPAWN_EGG.get());
                output.accept(ModItem.HOSTILE_INAZUMA_SPAWN_EGG.get());
                output.accept(ModBlock.COMMAND_CENTER.get());
            }).build());

}
