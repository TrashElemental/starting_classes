package net.trashelemental.starting_classes.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PlayerClassProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<PlayerClassData> PLAYER_CLASS =
            CapabilityManager.get(
                    new CapabilityToken<>() {}
            );

    private final PlayerClassData data = new PlayerClassData();

    private final LazyOptional<PlayerClassData> optional =
            LazyOptional.of(() -> data);

    @Override
    public <T> LazyOptional<T> getCapability(
            @NotNull Capability<T> cap,
            @Nullable Direction side
    ) {
        return cap == PLAYER_CLASS
                ? optional.cast()
                : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putString(
                "SelectedClass",
                data.getSelectedClass()
        );

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {

        data.setSelectedClass(
                tag.getString("SelectedClass")
        );
    }
}
