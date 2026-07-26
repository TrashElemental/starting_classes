package net.trashelemental.starting_classes.util.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.attachment.PlayerClassHelper;
import net.trashelemental.starting_classes.class_system.AttributeModifierEntry;
import net.trashelemental.starting_classes.class_system.EquipmentEntry;
import net.trashelemental.starting_classes.class_system.StartingClassData;
import net.trashelemental.starting_classes.class_system.StartingClassManager;
import net.trashelemental.starting_classes.client.LoginState;
import net.trashelemental.starting_classes.client.OpenClassSelectionPacket;
import net.trashelemental.starting_classes.client.StartingClassesNetworking;
import net.trashelemental.starting_classes.client.SyncClassSelectionPacket;
import net.trashelemental.starting_classes.util.ClassBlacklistReloadListener;
import net.trashelemental.starting_classes.util.EquipmentDistributor;
import net.trashelemental.starting_classes.util.StartingClassReloadListener;

@EventBusSubscriber(modid = "starting_classes", bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ClassBlacklistReloadListener());
        event.addListener(new StartingClassReloadListener(event.getRegistryAccess()));
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerTickEvent.Post event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (LoginState.hasSent(player)) {
            return;
        }

        LoginState.markSent(player);

        if (PlayerClassHelper.hasSelectedClass(player)) {
            String selectedClass = PlayerClassHelper.getSelectedClass(player);
            PacketDistributor.sendToPlayer(player, new SyncClassSelectionPacket(selectedClass));
        } else {
            PacketDistributor.sendToPlayer(player, new OpenClassSelectionPacket());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            LoginState.clear(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!PlayerClassHelper.hasSelectedClass(player)) {
            return;
        }

        String selectedClass = PlayerClassHelper.getSelectedClass(player);
        StartingClassData classData = StartingClassManager.getClassById(selectedClass);

        if (classData == null) {
            return;
        }

        for (EquipmentEntry entry : classData.equipment()) {
            if (entry instanceof AttributeModifierEntry attributeEntry) {
                EquipmentDistributor.applyAttributeModifier(player, attributeEntry);
            }
        }
    }
}