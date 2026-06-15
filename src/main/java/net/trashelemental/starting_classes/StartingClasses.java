package net.trashelemental.starting_classes;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.trashelemental.starting_classes.client.StartingClassesNetworking;
import org.slf4j.Logger;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(StartingClasses.MOD_ID)
public class StartingClasses
{
    public static final String MOD_ID = "starting_classes";

    public static final Logger LOGGER = LogUtils.getLogger();

    public StartingClasses()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        StartingClassesNetworking.register();

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
    
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {




        }
    }


    public static ResourceLocation prefix(String string) {
        return new ResourceLocation(MOD_ID, string);
    }

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tickDelay, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry<>(action, tickDelay));
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actionsToRun = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() <= 0) {
                    actionsToRun.add(work);
                }
            });
            actionsToRun.forEach(work -> work.getKey().run());
            workQueue.removeAll(actionsToRun);
        }
    }

}
