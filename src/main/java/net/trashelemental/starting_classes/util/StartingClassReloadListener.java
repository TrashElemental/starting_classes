package net.trashelemental.starting_classes.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.starting_classes.compat.ModCompatibility;
import net.trashelemental.starting_classes.menu.class_system.StartingClassData;
import net.trashelemental.starting_classes.menu.class_system.StartingClassManager;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class StartingClassReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new Gson();
    private static final String FOLDER = "classes";
    private static final Logger LOGGER = LogUtils.getLogger();

    public StartingClassReloadListener() {
        super(GSON, FOLDER);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager resourceManager, ProfilerFiller profiler) {

        StartingClassManager.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objects.entrySet()) {

            ResourceLocation fileId = entry.getKey();
            String path = fileId.getPath();

            // Check if this is a compatibility class
            if (path.startsWith("compat/")) {
                String modId = extractModIdFromPath(path);

                // Skip this class if the required mod isn't loaded
                if (modId != null && !ModCompatibility.isModLoaded(modId)) {
                    LOGGER.debug("Skipping class {} because mod {} is not loaded", fileId, modId);
                    continue;
                }
            }

            try {
                StartingClassData data = StartingClassData.CODEC
                        .parse(JsonOps.INSTANCE, entry.getValue())
                        .getOrThrow(false, msg -> {
                            throw new IllegalStateException("Failed to parse class " + fileId + ": " + msg);
                        });

                StartingClassManager.register(data);

            } catch (Exception e) {
                LOGGER.error("Error loading starting class {}", fileId, e);
            }
        }

        StartingClassManager.notifyLoadComplete();
    }

    private String extractModIdFromPath(String path) {
        String[] parts = path.split("/");
        if (parts.length >= 2 && parts[0].equals("compat")) {
            return parts[1];
        }
        return null;
    }
}