package net.trashelemental.starting_classes.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.trashelemental.starting_classes.class_system.StartingClassData;
import net.trashelemental.starting_classes.class_system.StartingClassManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncClassesPacket {
    private final List<StartingClassData> classes;

    public SyncClassesPacket(List<StartingClassData> classes) {
        this.classes = classes;
    }

    public static void encode(SyncClassesPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.classes.size());
        for (StartingClassData clazz : msg.classes) {
            buf.writeJsonWithCodec(StartingClassData.CODEC, clazz);
        }
    }

    public static SyncClassesPacket decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<StartingClassData> classes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            classes.add(buf.readJsonWithCodec(StartingClassData.CODEC));
        }
        return new SyncClassesPacket(classes);
    }

    public static void handle(SyncClassesPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

       // System.out.println("[CLIENT] Received SyncClassesPacket with " + msg.classes.size() + " classes");

        context.enqueueWork(() -> {
          //  System.out.println("[CLIENT] Processing SyncClassesPacket on client");
         //   System.out.println("[CLIENT] Thread: " + Thread.currentThread().getName());

            StartingClassManager.clear();
            for (StartingClassData clazz : msg.classes) {
               // System.out.println("[CLIENT] Registering class: " + clazz.id());
                StartingClassManager.register(clazz);
            }
            //System.out.println("[CLIENT] Classes registered on client: " + StartingClassManager.getClasses().size());
            StartingClassManager.notifyLoadComplete();
        });

        context.setPacketHandled(true);
    }
}