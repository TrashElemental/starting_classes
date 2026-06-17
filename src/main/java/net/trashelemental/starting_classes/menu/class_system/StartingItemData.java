package net.trashelemental.starting_classes.menu.class_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Optional;

public record StartingItemData(ResourceLocation itemId, int count, @Nullable DataComponentPatch components) {

    public ItemStack createStack() {

        Item item = BuiltInRegistries.ITEM.get(itemId);

        ItemStack stack = new ItemStack(item, count);

        if (components != null) {
            stack.applyComponents(components);
        }

        return stack;
    }

    public static final Codec<StartingItemData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC
                            .fieldOf("item")
                            .forGetter(StartingItemData::itemId),

                    Codec.INT
                            .optionalFieldOf("count", 1)
                            .forGetter(StartingItemData::count),

                    DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(data ->
                                    data.components() == null
                                            ? DataComponentPatch.EMPTY
                                            : data.components()
                            )

            ).apply(instance, StartingItemData::new));

}