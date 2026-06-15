package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;

public sealed interface EquipmentEntry permits FixedItemEntry, ChoiceItemEntry, MobEntry {

    public static final Codec<EquipmentEntry> CODEC =
            Codec.STRING.dispatch(
                    entry -> {
                        if (entry instanceof FixedItemEntry) return "fixed";
                        if (entry instanceof ChoiceItemEntry) return "choice";
                        if (entry instanceof MobEntry) return "mob";
                        throw new IllegalStateException("Unknown EquipmentEntry: " + entry.getClass());
                    },
                    type -> switch (type) {
                        case "fixed" -> FixedItemEntry.CODEC;
                        case "choice" -> ChoiceItemEntry.CODEC;
                        case "mob" -> MobEntry.CODEC;
                        default -> throw new IllegalArgumentException("Unknown equipment type: " + type);
                    }
            );

}
