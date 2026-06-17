package net.trashelemental.starting_classes.util.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.attachment.PlayerClassHelper;
import net.trashelemental.starting_classes.client.OpenClassSelectionPacket;
import net.trashelemental.starting_classes.client.StartingClassesNetworking;
import net.trashelemental.starting_classes.util.StartingClassReloadListener;

@EventBusSubscriber(modid = "starting_classes", bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new StartingClassReloadListener(event.getRegistryAccess()));
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (PlayerClassHelper.hasSelectedClass(player)) {
            return;
        }

        PacketDistributor.sendToPlayer(player, new OpenClassSelectionPacket());
    }
}