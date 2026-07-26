package net.trashelemental.starting_classes.util.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.trashelemental.starting_classes.class_system.ClassBlacklist;
import net.trashelemental.starting_classes.class_system.StartingClassManager;

public class ClassBlacklistCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // /starting_classes_disable_class "<classId>"
        dispatcher.register(
                Commands.literal("starting_classes_disable_class")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("classId", StringArgumentType.string())
                                .executes(context -> {
                                    String classId = StringArgumentType.getString(context, "classId");
                                    CommandSourceStack source = context.getSource();

                                    if (ClassBlacklist.isBlacklisted(classId)) {
                                        source.sendFailure(Component.literal("Class '" + classId + "' is already blacklisted."));
                                        return 0;
                                    }

                                    if (StartingClassManager.getClassById(classId) == null) {
                                        source.sendFailure(Component.literal("Class not found: " + classId));
                                        return 0;
                                    }

                                    ClassBlacklist.blacklist(classId);
                                    source.sendSuccess(
                                            () -> Component.literal("Class '" + classId + "' has been blacklisted."),
                                            true
                                    );

                                    return 1;
                                })
                        )
        );

        dispatcher.register(
                Commands.literal("starting_classes_enable_class")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("classId", StringArgumentType.string())
                                .executes(context -> {
                                    String classId = StringArgumentType.getString(context, "classId");
                                    CommandSourceStack source = context.getSource();

                                    if (!ClassBlacklist.isBlacklisted(classId)) {
                                        source.sendFailure(Component.literal("Class '" + classId + "' is not blacklisted."));
                                        return 0;
                                    }

                                    ClassBlacklist.unblacklist(classId);
                                    source.sendSuccess(
                                            () -> Component.literal("Class '" + classId + "' has been enabled."),
                                            true
                                    );

                                    return 1;
                                })
                        )
        );

        dispatcher.register(
                Commands.literal("starting_classes_clear_blacklist")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();

                            if (ClassBlacklist.getBlacklist().isEmpty()) {
                                source.sendFailure(Component.literal("The blacklist is already empty."));
                                return 0;
                            }

                            int count = ClassBlacklist.getBlacklist().size();
                            ClassBlacklist.clear();

                            source.sendSuccess(
                                    () -> Component.literal("Cleared blacklist. " + count + " class(es) have been enabled."),
                                    true
                            );

                            return 1;
                        })
        );
    }

}
