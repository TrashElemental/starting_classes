package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

public record MobData(ResourceLocation entityId, int count, @Nullable String nbtString) {

    public static final Codec<MobData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("entityId").forGetter(MobData::entityId),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(MobData::count),
                    Codec.STRING.optionalFieldOf("nbt").forGetter(data -> Optional.ofNullable(data.nbtString()))
            ).apply(instance, (entityId, count, nbtOpt) ->
                    new MobData(entityId, count, nbtOpt.orElse(null))
            ));

}
