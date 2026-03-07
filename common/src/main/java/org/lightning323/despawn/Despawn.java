package org.lightning323.despawn;

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

        entity.persistenceRequired =
                ConfigManager.INSTANCE.persistentMobs().contains(entityKey)
                || !hasDespawnableName(entity);
    }

    public static boolean hasDespawnableName(Mob entity) {
        if(entity.hasCustomName()) {
            return Utils.matchesStackedName(entity.getCustomName().getString(), entity);
        }
        return true;
    }
}
