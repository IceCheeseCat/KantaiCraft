package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.menu.commandcenter.CommandCenterMenu;
import com.github.icecheesecat.kantaicraft.menu.ship.ShipMenu;
import com.github.icecheesecat.kantaicraft.menu.shipyard.ShipyardMenu;
import com.mojang.brigadier.Command;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenu {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, KantaiCraft.MODID);

    public static final RegistryObject<MenuType<ShipMenu>> SHIP_MENU = MENUS.register("ship_menu", () -> IForgeMenuType.create(ShipMenu::new));
    public static final RegistryObject<MenuType<ShipyardMenu>> SHIPYARD_MENU = MENUS.register("shipyard_menu", () -> IForgeMenuType.create(ShipyardMenu::new));
    public static final RegistryObject<MenuType<CommandCenterMenu>> COMMAND_CENTER_MENU = MENUS.register("command_center_menu", () -> IForgeMenuType.create(CommandCenterMenu::new));

}
