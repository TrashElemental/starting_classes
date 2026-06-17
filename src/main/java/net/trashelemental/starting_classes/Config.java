package net.trashelemental.starting_classes;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = StartingClasses.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue STARTING_MOB_CUSTOM_HEALTH = BUILDER
            .comment("Should starting mobs have a custom amount of health (defined below)?")
            .define("Starting mobs have custom health:", false);
    public static final ModConfigSpec.IntValue STARTING_MOB_CUSTOM_HEALTH_AMOUNT = BUILDER
            .comment("If the above is true, how much health should starting mobs have?")
            .defineInRange("Starting mob custom health:", 40, 1, 999);
    public static final ModConfigSpec.BooleanValue STARTING_MOB_INFINITE_REGEN = BUILDER
            .comment("Should starting mobs have an infinite regeneration one effect?")
            .define("Starting mobs infinite regen:", false);



    static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        if (event.getConfig().getSpec() == Config.SPEC) {
        }

    }
}
