package net.trashelemental.starting_classes.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.trashelemental.starting_classes.StartingClasses;

@EventBusSubscriber(modid = StartingClasses.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientNetworking {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {

        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                OpenClassSelectionPacket.TYPE,
                OpenClassSelectionPacket.STREAM_CODEC,
                ClientPacketHandlers::handleOpenClassSelection
        );
    }
}