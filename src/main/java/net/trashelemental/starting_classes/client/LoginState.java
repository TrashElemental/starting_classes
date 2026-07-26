package net.trashelemental.starting_classes.client;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LoginState {

    private static final Set<UUID> SENT_SCREEN = new HashSet<>();

    public static boolean hasSent(ServerPlayer player) {
        return SENT_SCREEN.contains(player.getUUID());
    }

    public static void markSent(ServerPlayer player) {
        SENT_SCREEN.add(player.getUUID());
    }

    public static void clear(ServerPlayer player) {
        SENT_SCREEN.remove(player.getUUID());
    }
}
