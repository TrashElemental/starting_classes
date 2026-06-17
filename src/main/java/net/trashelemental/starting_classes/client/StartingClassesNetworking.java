package net.trashelemental.starting_classes.client;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.trashelemental.starting_classes.StartingClasses;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = StartingClasses.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class StartingClassesNetworking {

    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {

        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        registrar.playToServer(
                SelectClassPacket.TYPE,
                SelectClassPacket.STREAM_CODEC,
                SelectClassPacket::handle
        );
    }
}
