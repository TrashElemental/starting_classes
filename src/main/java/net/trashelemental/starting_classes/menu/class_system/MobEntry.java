package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

public record MobEntry(ResourceLocation entityId, int count, @Nullable String nbtString) implements EquipmentEntry {

    public static final MapCodec<MobEntry> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("entityId")
                            .forGetter(MobEntry::entityId),

                    Codec.INT.optionalFieldOf("count", 1)
                            .forGetter(MobEntry::count),

                    Codec.STRING.optionalFieldOf("nbt")
                            .forGetter(data -> Optional.ofNullable(data.nbtString()))
            ).apply(instance, (entityId, count, nbtOpt) ->
                    new MobEntry(entityId, count, nbtOpt.orElse(null))
            ));

}