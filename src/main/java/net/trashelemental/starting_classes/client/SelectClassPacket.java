package net.trashelemental.starting_classes.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.capability.PlayerClassHelper;
import net.trashelemental.starting_classes.menu.class_system.StartingClassData;
import net.trashelemental.starting_classes.menu.class_system.StartingClassManager;
import net.trashelemental.starting_classes.util.EquipmentDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record SelectClassPacket(String classId, List<Integer> choices) {

    public static void encode(SelectClassPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.classId());

        buf.writeVarInt(packet.choices().size());

        for (int choice : packet.choices()) {
            buf.writeVarInt(choice);
        }
    }

    public static SelectClassPacket decode(FriendlyByteBuf buf) {

        String classId = buf.readUtf();

        int size = buf.readVarInt();

        List<Integer> choices = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            choices.add(buf.readVarInt());
        }

        return new SelectClassPacket(classId, choices);
    }

    public static void handle(SelectClassPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {

        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            StartingClassData clazz = StartingClassManager.getClassById(packet.classId());

            if (clazz == null) return;
            if (player == null) return;

            EquipmentDistributor.distributeEquipment(player, clazz, packet.choices());
            PlayerClassHelper.setSelectedClass(player, clazz.id());
        });

        context.setPacketHandled(true);
    }

}
