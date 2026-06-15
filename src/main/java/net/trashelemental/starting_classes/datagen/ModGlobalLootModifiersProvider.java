package net.trashelemental.starting_classes.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.trashelemental.starting_classes.StartingClasses;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, StartingClasses.MOD_ID);
    }


    @Override
    protected void start() {


    }
}
