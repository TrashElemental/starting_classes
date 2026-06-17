package net.trashelemental.starting_classes.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.attachment.PlayerClassHelper;
import net.trashelemental.starting_classes.menu.class_system.StartingClassData;
import net.trashelemental.starting_classes.menu.class_system.StartingClassManager;
import net.trashelemental.starting_classes.util.EquipmentDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record SelectClassPacket(String classId, List<Integer> choices) implements CustomPacketPayload {

    public static final Type<SelectClassPacket> TYPE =
            new Type<>(StartingClasses.prefix("select_class"));

    public static final StreamCodec<FriendlyByteBuf, SelectClassPacket> STREAM_CODEC =
            StreamCodec.of(
                    SelectClassPacket::encode,
                    SelectClassPacket::decode
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static void encode(FriendlyByteBuf buf, SelectClassPacket packet) {

        buf.writeUtf(packet.classId());

        buf.writeVarInt(packet.choices().size());

        for (int choice : packet.choices()) {
            buf.writeVarInt(choice);
        }
    }

    private static SelectClassPacket decode(FriendlyByteBuf buf) {

        String classId = buf.readUtf();

        int size = buf.readVarInt();

        List<Integer> choices = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            choices.add(buf.readVarInt());
        }

        return new SelectClassPacket(classId, choices);
    }

    public static void handle(SelectClassPacket packet, IPayloadContext context) {

        ServerPlayer player = (ServerPlayer) context.player();

        StartingClassData clazz =
                StartingClassManager.getClassById(packet.classId());

        if (clazz == null) {
            return;
        }

        EquipmentDistributor.distributeEquipment(
                player,
                clazz,
                packet.choices()
        );

        PlayerClassHelper.setSelectedClass(
                player,
                clazz.id()
        );
    }
}
