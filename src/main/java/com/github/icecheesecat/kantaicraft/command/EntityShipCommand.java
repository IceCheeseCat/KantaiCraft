package com.github.icecheesecat.kantaicraft.command;

import com.github.icecheesecat.kantaicraft.capability.kantaidata.PlayerKantaiDataCapability;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

public class EntityShipCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> header() {
        return Commands.literal("entityShip");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createAddAllEntityShip() {
        return Commands.literal("addAll").requires(commandSourceStack -> commandSourceStack.hasPermission(2)).executes((commandSourceStack) -> {
            return addAllEntityShip(commandSourceStack.getSource());
        });
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createAdd(CommandBuildContext pContext) {
        return Commands.literal("add").requires(commandSourceStack -> commandSourceStack.hasPermission(2))
            .then(Commands.argument("entity", ResourceArgument.resource(pContext, ModEntity.ENTITY_TYPES.getRegistryKey())).executes((commandSourceStack) -> {
            return addEntity(commandSourceStack.getSource(), ResourceArgument.getSummonableEntityType(commandSourceStack, "entity"));
        }));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createRemoveAllEntityShip() {
        return Commands.literal("removeAll").requires(commandSourceStack -> commandSourceStack.hasPermission(2)).executes((commandSourceStack) -> {
            return removeAllEntityShip(commandSourceStack.getSource());
        });
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createListAllEntityShip() {
        return Commands.literal("listAll").requires(commandSourceStack -> commandSourceStack.hasPermission(0)).executes((commandSourceStack) -> {
            return listAllEntityShip(commandSourceStack.getSource());
        });
    }

    private static int addAllEntityShip(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        if (!player.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
            source.sendFailure(Component.literal("Player has no PlayerKantaiData capability"));
            return -1;
        }

        player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                    ModEntity.getAllPlayerShips().forEach(
                            entityType -> {
                                playerKantaiData.addShipInDock(entityType, (ServerLevel) player.level());
                                source.sendSuccess(() -> Component.literal("Added " + entityType + " to PlayerKantaiData"), true);
                            }
                    );
                }
        );

        return 1;

    }

    private static int addEntity(CommandSourceStack source, Holder.Reference<EntityType<?>> pType) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        if (!player.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
            source.sendFailure(Component.literal("Player has no PlayerKantaiData capability"));
            return -1;
        }

        player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                        EntityType<?> entityType = pType.value();

                        playerKantaiData.addShipInDock(entityType, (ServerLevel) player.level());
                        source.sendSuccess(() -> Component.literal("Added " + entityType + " to PlayerKantaiData"), true);
                }
        );

        return 1;

    }

    private static int removeAllEntityShip(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        if (!player.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
            source.sendFailure(Component.literal("Player has no PlayerKantaiData capability"));
            return -1;
        }

        player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                    playerKantaiData.getInDockShips().forEach(serializedEntityShip -> source.sendSuccess(() -> Component.literal("Removed " + serializedEntityShip.getEntityType() + " from PlayerKantaiData"), true));
                    playerKantaiData.getInDockShips().clear();
                }
        );

        return 1;

    }

    private static int listAllEntityShip(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        if (!player.getCapability(PlayerKantaiDataCapability.TOKEN).isPresent()) {
            source.sendFailure(Component.literal("Player has no PlayerKantaiData capability"));
            return -1;
        }

        player.getCapability(PlayerKantaiDataCapability.TOKEN).ifPresent(
                playerKantaiData -> {
                    String string = "";
                    for (int i = 0; i < playerKantaiData.getInDockShips().size(); i++) {
                        string = string.concat(i + " : " + playerKantaiData.getInDockShips().get(i).getEntityType().toString() + "\n");
                    }

                    source.sendSystemMessage(Component.literal(string));
                }
        );

        return 1;

    }

}
