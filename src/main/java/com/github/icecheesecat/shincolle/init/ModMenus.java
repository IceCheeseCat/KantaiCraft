package com.github.icecheesecat.shincolle.init;

import com.github.icecheesecat.shincolle.Shincolle;
import com.github.icecheesecat.shincolle.menu.ShipMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Shincolle.MODID);

    public static final RegistryObject<MenuType<ShipMenu>> SHIP_MENU = MENUS.register("ship_menu", () -> IForgeMenuType.create(ShipMenu::new));


}
