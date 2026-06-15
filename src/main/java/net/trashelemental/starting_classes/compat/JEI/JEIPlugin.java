package net.trashelemental.starting_classes.compat.JEI;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.starting_classes.StartingClasses;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(StartingClasses.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {



    }

}
