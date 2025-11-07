package com.github.icecheesecat.kantaicraft.command;

import com.github.icecheesecat.kantaicraft.capability.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.equipment.EquipmentManager;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class EquipmentCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> header() {
        return Commands.literal("equipment");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createAddAllEquipment() {
        return Commands.literal("addAll").requires(commandSourceStack -> commandSourceStack.hasPermission(2)).executes((commandSourceStack) -> {
            return addAllEquipments(commandSourceStack.getSource());
        });
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createRemoveAllEquipment() {
        return Commands.literal("removeAll").requires(commandSourceStack -> commandSourceStack.hasPermission(2)).executes((commandSourceStack) -> {
            return removeAllEquipments(commandSourceStack.getSource());
        });
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createListAllEquipment() {
        return Commands.literal("listAll").requires(commandSourceStack -> commandSourceStack.hasPermission(2)).executes((commandSourceStack) -> {
            return listAllEquipment(commandSourceStack.getSource());
        });
    }

    private static int addAllEquipments(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        if (!player.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
            source.sendFailure(Component.literal("Player has no PlayerKantaiData capability"));
            return -1;
        }

        player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                    EquipmentManager.getAllEquipmentTypes().forEach((equipmentType) -> {
                        playerKantaiData.addEquipment(equipmentType.create());
                        source.sendSystemMessage(Component.literal("add new equipment -> " + equipmentType));
                    });
                }
        );

        return 1;
    }

    private static int removeAllEquipments(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        if (!player.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
            source.sendFailure(Component.literal("Player has no PlayerKantaiData capability"));
            return -1;
        }

        player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                    playerKantaiData.getEquipments().forEach(equipment -> {
                            source.sendSystemMessage(Component.literal("Removed equipment -> " + equipment));
                        }
                    );
                    playerKantaiData.getEquipments().clear();
                }
        );

        return 1;
    }

    private static int listAllEquipment(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        if (!player.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
            source.sendFailure(Component.literal("Player has no PlayerKantaiData capability"));
            return -1;
        }

        player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                    String string = "";
                    for (int i = 0; i < playerKantaiData.getEquipments().size(); i++) {
                        string = string.concat(i + " : " + playerKantaiData.getEquipments().get(i) + "\n");
                    }

                    source.sendSystemMessage(Component.literal(string));
                }
        );

        return 1;
    }

}
