package net.trashelemental.starting_classes.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.capability.PlayerClassHelper;
import net.trashelemental.starting_classes.menu.ClassSelectionScreen;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = StartingClasses.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    private static KeyMapping openClassSelectionKey;
    private static boolean wasKeyDown = false;

//    @SubscribeEvent
//    public static void onRegisterKeybinds(RegisterKeyMappingsEvent event) {
//        openClassSelectionKey = new KeyMapping(
//                "key.starting_classes.open_class_selection",
//                GLFW.GLFW_KEY_C,
//                "key.categories.starting_classes"
//        );
//        event.register(openClassSelectionKey);
//    }

    @Mod.EventBusSubscriber(modid = StartingClasses.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeBusEvents {

//        @SubscribeEvent
//        public static void onKeyInput(InputEvent.Key event) {
//            if (openClassSelectionKey == null) {
//                return;
//            }
//
//            Minecraft minecraft = Minecraft.getInstance();
//
//            if (minecraft.player == null || minecraft.level == null) {
//                wasKeyDown = false;
//                return;
//            }
//
//            LocalPlayer player = minecraft.player;
//
//            boolean isKeyDown = openClassSelectionKey.isDown();
//
//            if (isKeyDown && !wasKeyDown) {
//                if (PlayerClassHelper.hasSelectedClass(player)) {
//                    wasKeyDown = true;
//                    return;
//                }
//
//                if (minecraft.screen instanceof ClassSelectionScreen) {
//                    wasKeyDown = true;
//                    return;
//                }
//
//                // Open the class selection screen
//                minecraft.setScreen(new ClassSelectionScreen());
//            }
//
//            wasKeyDown = isKeyDown;
//        }

        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Minecraft mc = Minecraft.getInstance();

                if (!ClientState.AWAITING_CLASS_SELECTION.get()) return;
                if (mc.player == null) return;
                if (mc.screen != null) return;

                if (ClientState.AWAITING_CLASS_SELECTION.get()) {
                 //   System.out.println(
                //            "[CLIENT] Awaiting screen. player=" + (mc.player != null)
               //                     + " screen=" + mc.screen
                //    );
                }

                LocalPlayer player = mc.player;

                if (PlayerClassHelper.hasSelectedClass(player)) {
                    ClientState.AWAITING_CLASS_SELECTION.set(false);
                    return;
                }

                mc.setScreen(new ClassSelectionScreen());
                ClientState.AWAITING_CLASS_SELECTION.set(false);

               // System.out.println("[CLIENT] Packet handled on thread: " + Thread.currentThread().getName());
            }
        }
    }
}