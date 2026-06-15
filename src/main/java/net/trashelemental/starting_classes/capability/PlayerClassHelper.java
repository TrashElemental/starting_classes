package net.trashelemental.starting_classes.capability;

import net.minecraft.world.entity.player.Player;

public class PlayerClassHelper {

    public static String getSelectedClass(Player player) {
        return player.getCapability(PlayerClassProvider.PLAYER_CLASS).map(PlayerClassData::getSelectedClass).orElse("");
    }

    public static void setSelectedClass(Player player, String classId) {
        player.getCapability(PlayerClassProvider.PLAYER_CLASS).ifPresent(data -> data.setSelectedClass(classId));
    }

    public static boolean hasSelectedClass(Player player) {
        return player.getCapability(PlayerClassProvider.PLAYER_CLASS).map(PlayerClassData::hasSelectedClass).orElse(false);
    }
}
