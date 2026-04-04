package org.lightning323.performancetweaks.optimizations.despawn;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.lightning323.performancetweaks.config.ConfigManager;

public final class Despawn {

//    public static void init() {
//        Almanac.addCommandRegistration(DespawnCommands::register);
//    }

    public static void setPersistence(Mob entity, EquipmentSlot slot) {
        ItemStack itemStack = entity.getItemBySlot(slot);

        // Vanilla 1.20.1 way to handle NBT in common code
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.putBoolean("picked", true);

        // Use BuiltInRegistries for common registry access in 1.20.1
        String entityKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();

        //TODO: We have to use a mixin because we can't access the persistenceRequired field
        /*
         * https://discord.com/channels/752944675425353768/1050532463090335744/1486481311668502621
         * 1. I would recommend using mixin accessors for fields / methods
         * 2. ATs have always been a weird thing, I am guessing you have an error somewhere that forge is just not showing (Its working on fabric)
         */
        MobMixin_I entityMixin = (MobMixin_I) entity;
        entityMixin.setPersistenceRequired(ConfigManager.INSTANCE.persistentMobs().contains(entityKey)
                || !hasDespawnableName(entity));
    }

    public static boolean hasDespawnableName(Mob entity) {
        if (entity.hasCustomName()) {
            return Utils.matchesStackedName(entity.getCustomName().getString(), entity);
        }
        return true;
    }
}
