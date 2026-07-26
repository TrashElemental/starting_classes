package net.trashelemental.starting_classes.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CommandEntry(String command) implements EquipmentEntry {

    public static final MapCodec<CommandEntry> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.fieldOf("command").forGetter(CommandEntry::command)
            ).apply(instance, CommandEntry::new));

}
