package net.trashelemental.starting_classes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.menu.ClassSelectionScreen;

import java.util.function.Supplier;

public record OpenClassSelectionPacket() implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, OpenClassSelectionPacket> STREAM_CODEC =
            StreamCodec.unit(new OpenClassSelectionPacket());

    public static final Type<OpenClassSelectionPacket> TYPE =
            new Type<>(StartingClasses.prefix("open_class_selection"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenClassSelectionPacket packet, IPayloadContext context) {
        context.enqueueWork(() ->
                Minecraft.getInstance().setScreen(new ClassSelectionScreen())
        );
    }
}
