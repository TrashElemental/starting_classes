package net.trashelemental.starting_classes.attachment;

import net.minecraft.world.entity.player.Player;

public class PlayerClassHelper {

    public static String getSelectedClass(Player player) {
        return player.getData(AttachmentsRegistry.CLASS_DATA.get())
                .getSelectedClass();
    }

    public static void setSelectedClass(Player player, String classId) {

        player.getData(AttachmentsRegistry.CLASS_DATA.get())
                .setSelectedClass(classId);
    }

    public static boolean hasSelectedClass(Player player) {

        return player.getData(AttachmentsRegistry.CLASS_DATA.get())
                .hasSelectedClass();
    }

}
