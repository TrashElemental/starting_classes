package net.trashelemental.starting_classes.util.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;
import net.trashelemental.starting_classes.client.ForceOpenClassSelectionPacket;
import net.trashelemental.starting_classes.client.StartingClassesNetworking;

public class ClassSelectionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("starting_classes_open_menu")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    CommandSourceStack source = context.getSource();

                                    StartingClassesNetworking.CHANNEL.send(
                                            PacketDistributor.PLAYER.with(() -> player),
                                            new ForceOpenClassSelectionPacket()
                                    );

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