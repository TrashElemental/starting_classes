package net.trashelemental.starting_classes.util.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.trashelemental.starting_classes.client.ForceOpenClassSelectionPacket;


public class ClassSelectionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("starting_classes_open_menu")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("playerName", EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "playerName");
                                    CommandSourceStack source = context.getSource();

                                    PacketDistributor.sendToPlayer(player, new ForceOpenClassSelectionPacket());

                                    source.sendSuccess(
                                            () -> Component.literal("Opened class selection menu for " + player.getName().getString()),
                                            true
                                    );

                                    return 1;
                                })
                        )
        );
    }

}