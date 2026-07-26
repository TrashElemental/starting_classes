package net.trashelemental.starting_classes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.menu.ClassSelectionScreen;

import java.util.function.Supplier;

public record ForceOpenClassSelectionPacket() implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ForceOpenClassSelectionPacket> STREAM_CODEC =
            StreamCodec.unit(new ForceOpenClassSelectionPacket());

    public static final Type<ForceOpenClassSelectionPacket> TYPE =
            new Type<>(StartingClasses.prefix("force_open_class_selection"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ForceOpenClassSelectionPacket packet, IPayloadContext context) {
        context.enqueueWork(() ->
                Minecraft.getInstance().setScreen(new ClassSelectionScreen())
        );
    }
}
