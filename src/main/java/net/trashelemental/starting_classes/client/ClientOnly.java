package net.trashelemental.starting_classes.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.trashelemental.starting_classes.StartingClasses;
import net.trashelemental.starting_classes.menu.ClassSelectionScreen;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClientOnly {

    public static void openClassScreen() {
        Minecraft.getInstance().setScreen(new ClassSelectionScreen());
    }
}