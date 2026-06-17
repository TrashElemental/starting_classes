package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeBonusData(
        ResourceLocation attribute,
        double amount,
        AttributeModifier.Operation operation
) {

    public static final Codec<AttributeModifier.Operation> OPERATION_CODEC =
            Codec.STRING.xmap(
                    str -> AttributeModifier.Operation.valueOf(str.toUpperCase()),
                    op -> op.name().toLowerCase()
            );

    public static final Codec<AttributeBonusData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("attribute").forGetter(AttributeBonusData::attribute),
                    Codec.DOUBLE.fieldOf("amount").forGetter(AttributeBonusData::amount),
                    OPERATION_CODEC.fieldOf("operation").forGetter(AttributeBonusData::operation)
            ).apply(instance, AttributeBonusData::new));

}
