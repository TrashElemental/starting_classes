package net.trashelemental.starting_classes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.trashelemental.starting_classes.menu.ClassSelectionScreen;

import java.util.function.Supplier;

public record OpenClassSelectionPacket() {

    public static void encode(OpenClassSelectionPacket msg, FriendlyByteBuf buf) {
        // nothing
    }

    public static OpenClassSelectionPacket decode(FriendlyByteBuf buf) {
        return new OpenClassSelectionPacket();
    }

    public static void handle(OpenClassSelectionPacket msg, Supplier<NetworkEvent.Context> ctx) {

        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            Minecraft.getInstance().setScreen(new ClassSelectionScreen());
        });

        context.setPacketHandled(true);
    }
}
