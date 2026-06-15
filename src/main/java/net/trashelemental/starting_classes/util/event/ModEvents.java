package net.trashelemental.starting_classes.util.event;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.capability.PlayerClassHelper;
import net.trashelemental.starting_classes.capability.PlayerClassProvider;
import net.trashelemental.starting_classes.client.OpenClassSelectionPacket;
import net.trashelemental.starting_classes.client.StartingClassesNetworking;
import net.trashelemental.starting_classes.menu.ClassSelectionScreen;
import net.trashelemental.starting_classes.util.StartingClassReloadListener;

@Mod.EventBusSubscriber(modid = "starting_classes", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    private static final StartingClassReloadListener LISTENER_INSTANCE = new StartingClassReloadListener();

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(LISTENER_INSTANCE);
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player)) {
            return;
        }
        event.addCapability(StartingClasses.prefix("player_class"), new PlayerClassProvider());
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();

        event.getOriginal().getCapability(PlayerClassProvider.PLAYER_CLASS).ifPresent(oldData -> {
                    event.getEntity().getCapability(PlayerClassProvider.PLAYER_CLASS).ifPresent(newData -> {
                        newData.setSelectedClass(oldData.getSelectedClass());
                    });
                });

        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (PlayerClassHelper.hasSelectedClass(player)) {
            return;
        }

        StartingClassesNetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new OpenClassSelectionPacket());

    }
}