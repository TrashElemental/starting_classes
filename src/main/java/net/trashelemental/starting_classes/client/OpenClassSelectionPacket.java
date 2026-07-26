package net.trashelemental.starting_classes.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.trashelemental.starting_classes.StartingClasses;

import java.util.function.Supplier;

public record OpenClassSelectionPacket() {

    public static void encode(OpenClassSelectionPacket msg, FriendlyByteBuf buf) {}

    public static OpenClassSelectionPacket decode(FriendlyByteBuf buf) {
        return new OpenClassSelectionPacket();
    }

    public static void handle(OpenClassSelectionPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
          //  System.out.println("[CLIENT] Received OpenClassSelectionPacket");
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientState.AWAITING_CLASS_SELECTION.set(true));
           // System.out.println("[CLIENT] Setting awaiting flag");
           // System.out.println("[CLIENT] Packet handled on thread: " + Thread.currentThread().getName());
        });

        context.setPacketHandled(true);
    }
}