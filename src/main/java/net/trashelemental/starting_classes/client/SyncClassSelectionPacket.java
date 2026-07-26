package net.trashelemental.starting_classes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.attachment.PlayerClassHelper;
import net.trashelemental.starting_classes.menu.ClassSelectionScreen;

public record SyncClassSelectionPacket(String classId) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, SyncClassSelectionPacket> STREAM_CODEC =
            StreamCodec.of(
                    SyncClassSelectionPacket::encode,
                    SyncClassSelectionPacket::decode
            );

    public static final Type<SyncClassSelectionPacket> TYPE =
            new Type<>(StartingClasses.prefix("sync_class_selection"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static void encode(FriendlyByteBuf buf, SyncClassSelectionPacket packet) {
        buf.writeUtf(packet.classId());
    }

    private static SyncClassSelectionPacket decode(FriendlyByteBuf buf) {
        String classId = buf.readUtf();
        return new SyncClassSelectionPacket(classId);
    }

    public static void handle(SyncClassSelectionPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                PlayerClassHelper.setSelectedClass(minecraft.player, packet.classId());
                if (minecraft.screen instanceof ClassSelectionScreen) {
                    minecraft.setScreen(null);
                }
            }
        });
    }
}
