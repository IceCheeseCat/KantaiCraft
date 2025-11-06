package com.github.icecheesecat.kantaicraft.command;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {
        dispatcher.register(Commands.literal(KantaiCraft.MODID)
                .then(EntityShipCommand.createAddAllEntityShip())
                .then(EntityShipCommand.createAdd(pContext))
                .then(EntityShipCommand.createRemoveAllEntityShip())
                .then(EntityShipCommand.createListAllEntityShip())
        );
    }


}
