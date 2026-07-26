package net.trashelemental.starting_classes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.trashelemental.starting_classes.menu.ClassSelectionScreen;

import java.util.function.Supplier;

public record ForceOpenClassSelectionPacket() {

    public static void encode(ForceOpenClassSelectionPacket msg, FriendlyByteBuf buf) {}

    public static ForceOpenClassSelectionPacket decode(FriendlyByteBuf buf) {
        return new ForceOpenClassSelectionPacket();
    }

    public static void handle(ForceOpenClassSelectionPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                    Minecraft.getInstance().setScreen(new ClassSelectionScreen())
            );
        });

        context.setPacketHandled(true);
    }
}
