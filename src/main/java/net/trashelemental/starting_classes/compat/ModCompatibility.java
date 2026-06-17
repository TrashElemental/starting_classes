package net.trashelemental.starting_classes.compat;


import net.neoforged.fml.ModList;

public class ModCompatibility {

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }


    public static String getModIdFromResourceLocation(String resourceLocationPath) {
        // resourceLocationPath format: "compat/modname/classname"
        String[] parts = resourceLocationPath.split("/");
        if (parts.length >= 2 && parts[0].equals("compat")) {
            return parts[1];
        }
        return null;
    }
}
