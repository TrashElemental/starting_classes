package net.trashelemental.starting_classes.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.menu.ClassSelectionScreen;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandlers {

    public static void handleOpenClassSelection(
            OpenClassSelectionPacket packet,
            IPayloadContext context
    ) {
        context.enqueueWork(() ->
                Minecraft.getInstance()
                        .setScreen(new ClassSelectionScreen())
        );
    }
}
