package com.github.icecheesecat.shincolle.init;

import com.github.icecheesecat.shincolle.Shincolle;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Shincolle.MODID);
    public static final RegistryObject<CreativeModeTab> TAB_0 = CREATIVE_MODE_TABS.register("tab_0", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.tab_0"))
            .icon(() -> ModItems.Grudge_0.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.Grudge_0.get());
                output.accept(ModItems.Grudge_1.get());
                output.accept(ModItems.LightAmmo.get());
                output.accept(ModItems.LightAmmoContainer.get());
                output.accept(ModItems.HeavyAmmo.get());
                output.accept(ModItems.HeavyAmmoContainer.get());
                output.accept(ModItems.AbyssiumIngot.get());
                output.accept(ModItems.AbyssiumNugget.get());
                output.accept(ModItems.PolymetallicNodules.get());
                output.accept(ModItems.PolymetallicNugget.get());
            }).build());

}
