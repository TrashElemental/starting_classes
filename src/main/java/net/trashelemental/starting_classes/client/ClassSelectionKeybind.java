package net.trashelemental.starting_classes.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.attachment.PlayerClassHelper;
import net.trashelemental.starting_classes.menu.ClassSelectionScreen;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = StartingClasses.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClassSelectionKeybind {

    private static KeyMapping openClassSelectionKey;
    private static boolean wasKeyDown = false;

    @SubscribeEvent
    public static void onRegisterKeybinds(RegisterKeyMappingsEvent event) {
        openClassSelectionKey = new KeyMapping(
                "key.starting_classes.open_class_selection",
                GLFW.GLFW_KEY_C,
                "key.categories.starting_classes"
        );
        event.register(openClassSelectionKey);
    }

    @EventBusSubscriber(modid = StartingClasses.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (openClassSelectionKey == null) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.player == null || minecraft.level == null) {
                wasKeyDown = false;
                return;
            }

            LocalPlayer player = minecraft.player;

            boolean isKeyDown = openClassSelectionKey.isDown();

            if (isKeyDown && !wasKeyDown) {
                if (PlayerClassHelper.hasSelectedClass(player)) {
                    wasKeyDown = true;
                    return;
                }

                if (minecraft.screen instanceof ClassSelectionScreen) {
                    wasKeyDown = true;
                    return;
                }

                minecraft.setScreen(new ClassSelectionScreen());
            }

            wasKeyDown = isKeyDown;
        }
    }
}