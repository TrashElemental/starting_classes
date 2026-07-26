package net.trashelemental.starting_classes.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.UUID;

public record AttributeModifierEntry(AttributeBonusData bonus) implements EquipmentEntry {

    public static final MapCodec<AttributeModifierEntry> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    AttributeBonusData.CODEC.fieldOf("bonus").forGetter(AttributeModifierEntry::bonus)
            ).apply(instance, AttributeModifierEntry::new));

    /**
     * Generates a deterministic UUID for this attribute modifier based on its content.
     * This ensures the same modifier gets the same UUID across respawns.
     */
    public UUID getDeterministicUUID() {
        return UUID.nameUUIDFromBytes(
                (bonus.attribute().toString() + bonus.amount() + bonus.operation()).getBytes()
        );
    }

}