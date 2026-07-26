package net.trashelemental.starting_classes.class_system;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.starting_classes.StartingClasses;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages a blacklist of class IDs that should not be loaded.
 * Pack makers can edit the blacklist JSON files to disable specific classes.
 */
public class ClassBlacklist {

    private static final Set<String> BLACKLISTED_CLASSES = new HashSet<>();
    private static boolean loaded = false;

    /**
     * Loads blacklisted class IDs from JSON configuration files.
     * Files should be located at: data/starting_classes/disabled_classes.json
     *
     * File format:
     * {
     *   "blacklist": [
     *     "class_id_1",
     *     "class_id_2"
     *   ]
     * }
     */
    public static void load(ResourceManager resourceManager) {
        BLACKLISTED_CLASSES.clear();

        ResourceLocation blacklistLocation =
                new ResourceLocation(StartingClasses.MOD_ID, "disabled_classes.json");

        resourceManager.getResource(blacklistLocation).ifPresent(resource -> {
            try (Reader reader = resource.openAsReader()) {

                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                if (json.has("disabled_classes")) {
                    JsonArray blacklist = json.getAsJsonArray("disabled_classes");

                    for (JsonElement element : blacklist) {
                        if (element.isJsonPrimitive()) {
                            String id = element.getAsString();
                            BLACKLISTED_CLASSES.add(id);
                            StartingClasses.LOGGER.info("Blacklisted class: {}", id);
                        }
                    }
                }

                StartingClasses.LOGGER.info(
                        "Loaded {} blacklisted classes",
                        BLACKLISTED_CLASSES.size()
                );

            } catch (IOException e) {
                StartingClasses.LOGGER.error(
                        "Failed to load disabled_classes.json",
                        e
                );
            }
        });

        loaded = true;
    }

    /**
     * Checks if a class ID is blacklisted.
     *
     * @param classId the class ID to check
     * @return true if the class is blacklisted, false otherwise
     */
    public static boolean isBlacklisted(String classId) {
        return BLACKLISTED_CLASSES.contains(classId);
    }

    /**
     * Gets the current set of blacklisted class IDs.
     * Modifiable for runtime adjustments if needed.
     *
     * @return a copy of the set of blacklisted class IDs
     */
    public static Set<String> getBlacklist() {
        return new HashSet<>(BLACKLISTED_CLASSES);
    }

    /**
     * Adds a class ID to the blacklist at runtime.
     *
     * @param classId the class ID to blacklist
     */
    public static void blacklist(String classId) {
        BLACKLISTED_CLASSES.add(classId);
    }

    /**
     * Removes a class ID from the blacklist at runtime.
     *
     * @param classId the class ID to un-blacklist
     */
    public static void unblacklist(String classId) {
        BLACKLISTED_CLASSES.remove(classId);
    }

    /**
     * Clears all blacklisted classes.
     */
    public static void clear() {
        BLACKLISTED_CLASSES.clear();
    }

    public static boolean isLoaded() {
        return loaded;
    }
}