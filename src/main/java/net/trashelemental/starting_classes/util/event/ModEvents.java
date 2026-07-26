package net.trashelemental.starting_classes.util.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.capability.PlayerClassHelper;
import net.trashelemental.starting_classes.capability.PlayerClassProvider;
import net.trashelemental.starting_classes.class_system.*;
import net.trashelemental.starting_classes.client.OpenClassSelectionPacket;
import net.trashelemental.starting_classes.client.StartingClassesNetworking;
import net.trashelemental.starting_classes.client.SyncClassesPacket;
import net.trashelemental.starting_classes.client.SyncPlayerClassPacket;
import net.trashelemental.starting_classes.util.ClassBlacklistReloadListener;
import net.trashelemental.starting_classes.util.StartingClassReloadListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "starting_classes", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    private static final StartingClassReloadListener LISTENER_INSTANCE = new StartingClassReloadListener();
    private static final Map<ServerPlayer, Integer> playersAwaitingSync = new HashMap<>();

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ClassBlacklistReloadListener());
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

        if (!(event.getEntity() instanceof ServerPlayer player))
            return;

//        System.out.println("[SERVER] PlayerLoggedIn: " + player.getName().getString());
//        System.out.println("[SERVER] Classes available on server: " + StartingClassManager.getClasses().size());

        playersAwaitingSync.put(player, 1);
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        Iterator<Map.Entry<ServerPlayer, Integer>> iterator = playersAwaitingSync.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ServerPlayer, Integer> entry = iterator.next();
            ServerPlayer player = entry.getKey();
            int ticksWaited = entry.getValue();

            if (ticksWaited >= 2) {
//                System.out.println("[SERVER] Syncing classes to: " + player.getName().getString());
//                System.out.println("[SERVER] Classes to sync: " + StartingClassManager.getClasses().size());
//                System.out.println("[SERVER] Player connection: " + player.connection.getClass().getSimpleName());

                try {
                    StartingClassesNetworking.CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            new SyncClassesPacket(StartingClassManager.getClasses())
                    );
                    //System.out.println("[SERVER] SyncClassesPacket sent successfully");
                } catch (Exception e) {
                    //System.out.println("[SERVER] ERROR sending SyncClassesPacket:");
                    e.printStackTrace();
                }

                if (PlayerClassHelper.hasSelectedClass(player)) {
                    //System.out.println("[SERVER] Player already has class selected");
                    iterator.remove();
                    continue;
                }

                //System.out.println("[SERVER] Has class: false");
                //System.out.println("[SERVER] Sending OpenClassSelectionPacket");

                StartingClassesNetworking.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new OpenClassSelectionPacket()
                );

                iterator.remove();
            } else {
                entry.setValue(ticksWaited + 1);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;

        StartingClassesNetworking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncPlayerClassPacket(PlayerClassHelper.getSelectedClass(player))
        );

        String selectedClass = PlayerClassHelper.getSelectedClass(player);
        if (selectedClass != null && !selectedClass.isEmpty()) {
            StartingClassData classData = StartingClassManager.getClassById(selectedClass);
            if (classData != null) {
                reapplyAttributeModifiers(player, classData);
            }
        }
    }

    /**
     * Reapplies all attribute modifiers from equipment entries in a class
     */
    private static void reapplyAttributeModifiers(ServerPlayer player, StartingClassData classData) {
        for (EquipmentEntry entry : classData.equipment()) {
            if (entry instanceof AttributeModifierEntry attr) {
                AttributeBonusData bonus = attr.bonus();
                var attribute = net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES.getValue(bonus.attribute());
                if (attribute != null) {
                    java.util.UUID modifierUUID = java.util.UUID.nameUUIDFromBytes(
                            ("starting_classes:" + bonus.attribute().toString()).getBytes(java.nio.charset.StandardCharsets.UTF_8)
                    );
                    var modifier = new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                            modifierUUID,
                            bonus.attribute().getPath(),
                            bonus.amount(),
                            bonus.operation()
                    );
                    net.trashelemental.starting_classes.junkyard_lib.util.UtilMethods.applyModifier(player, attribute, modifier);
                }
            }
        }
    }
}