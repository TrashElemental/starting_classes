package net.trashelemental.starting_classes.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.capability.PlayerClassHelper;

import java.util.function.Supplier;

public record SyncPlayerClassPacket(String classId) {

    public static void encode(SyncPlayerClassPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.classId());
    }

    public static SyncPlayerClassPacket decode(FriendlyByteBuf buf) {
        return new SyncPlayerClassPacket(buf.readUtf());
    }

    public static void handle(SyncPlayerClassPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (msg.classId() != null && !msg.classId().isEmpty()) {
                    var minecraft = net.minecraft.client.Minecraft.getInstance();
                    if (minecraft.player != null) {
                        PlayerClassHelper.setSelectedClass(minecraft.player, msg.classId());
                    }
                }
            });
        });

        context.setPacketHandled(true);
    }
}