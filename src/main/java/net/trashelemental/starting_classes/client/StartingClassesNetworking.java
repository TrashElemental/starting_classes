package net.trashelemental.starting_classes.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.trashelemental.starting_classes.StartingClasses;

import java.util.ArrayList;
import java.util.List;

public class StartingClassesNetworking {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL =
            NetworkRegistry.newSimpleChannel(
                    StartingClasses.prefix("main"),
                    () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals,
                    PROTOCOL_VERSION::equals
            );

    private static int packetId = 0;

    public static void register() {
        CHANNEL.registerMessage(
                packetId++,
                SelectClassPacket.class,
                SelectClassPacket::encode,
                SelectClassPacket::decode,
                SelectClassPacket::handle
        );

        CHANNEL.registerMessage(
                packetId++,
                OpenClassSelectionPacket.class,
                OpenClassSelectionPacket::encode,
                OpenClassSelectionPacket::decode,
                OpenClassSelectionPacket::handle
        );
    }
}
