package net.trashelemental.starting_classes.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.trashelemental.starting_classes.Config;
import org.slf4j.LoggerFactory;

public class TamedMobUtil {

    /**
     * Spawn an entity at the player's location and tame it if possible
     */
    public static void spawnAndTameMob(Player player, EntityType<?> entityType) {
        spawnAndTameMob(player, entityType, null);
    }

    /**
     * Spawn an entity at the player's location, apply NBT data, and tame it if possible
     * @param player The player to spawn the mob for
     * @param entityType The type of entity to spawn
     * @param nbtString Optional SNBT string to apply to the entity (can be null)
     */
    public static void spawnAndTameMob(Player player, EntityType<?> entityType, String nbtString) {
        if (entityType == null || player == null) {
            return;
        }

        Level level = player.level();

        try {
            Entity entity = entityType.create(level);

            if (entity == null) {
                LoggerFactory.getLogger(TamedMobUtil.class)
                        .warn("Failed to create entity: {}", entityType.getDescription().getString());
                return;
            }

            if (nbtString != null && !nbtString.isEmpty()) {
                try {
                    CompoundTag tag = TagParser.parseTag(nbtString);
                    entity.load(tag);
                } catch (Exception e) {
                    LoggerFactory.getLogger(TamedMobUtil.class)
                            .warn("Failed to apply NBT data to entity {}: {}",
                                    entityType.getDescription().getString(), e.getMessage());
                }
            }

            // Set position to player's location
            entity.setPos(player.getX(), player.getY(), player.getZ());

            // Tame the entity if it's tamable
            if (entity instanceof TamableAnimal tamable) {
                tamable.tame(player);
                tamable.setOwnerUUID(player.getUUID());
            }

            // Why the heck aren't horses TamableAnimals?
            if (entity instanceof AbstractHorse horse) {
                horse.setOwnerUUID(player.getUUID());
                horse.setTamed(true);
            }

            if (Config.STARTING_MOB_CUSTOM_HEALTH.get() && entity instanceof LivingEntity entityLiving) {
                entityLiving.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Config.STARTING_MOB_CUSTOM_HEALTH_AMOUNT.get());
                entityLiving.setHealth((float) Config.STARTING_MOB_CUSTOM_HEALTH_AMOUNT.get());
            }

            if (Config.STARTING_MOB_INFINITE_REGEN.get() && entity instanceof LivingEntity entityLiving2) {
                MobEffectInstance effect = new MobEffectInstance(MobEffects.REGENERATION, Integer.MAX_VALUE, 0, true, false, false);
                entityLiving2.addEffect(effect);
            }

            // Add entity to world
            level.addFreshEntity(entity);

        } catch (Exception e) {
            LoggerFactory.getLogger(TamedMobUtil.class)
                    .error("Error spawning mob {}", entityType.getDescription().getString(), e);
        }
    }
}