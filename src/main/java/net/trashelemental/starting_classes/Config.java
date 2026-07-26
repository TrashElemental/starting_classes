package net.trashelemental.starting_classes;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = StartingClasses.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue STARTING_MOB_CUSTOM_HEALTH = BUILDER
            .comment("Should starting mobs have a custom amount of health (defined below)?")
            .define("Starting mobs have custom health:", false);
    public static final ForgeConfigSpec.IntValue STARTING_MOB_CUSTOM_HEALTH_AMOUNT = BUILDER
            .comment("If the above is true, how much health should starting mobs have?")
            .defineInRange("Starting mob custom health:", 40, 1, 999);
    public static final ForgeConfigSpec.BooleanValue STARTING_MOB_INFINITE_REGEN = BUILDER
            .comment("Should starting mobs have an infinite regeneration one effect?")
            .define("Starting mobs infinite regen:", false);

    public static final ForgeConfigSpec.BooleanValue BLACKLIST_AFTER_CHOICE = BUILDER
            .comment("Should a class be disabled from selection after it has been chosen a certain number of times?")
            .define("Disable classes after selection:", false);
    public static final ForgeConfigSpec.IntValue BLACKLIST_AFTER_CHOICE_NUMBER = BUILDER
            .comment("If the above is true, how many times can a class be chosen by players before it is disabled?")
            .defineInRange("Number of times a class can be chosen before it is disabled:", 1, 1, 999);


    static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        if (event.getConfig().getSpec() == Config.SPEC) {
        }

    }
}
