package org.lightning323.frikinjay.letmedespawn;

import net.minecraft.core.registries.BuiltInRegistries;
import org.lightning323.frikinjay.almanac.Almanac;
import org.lightning323.frikinjay.letmedespawn.command.LetMeDespawnCommands;
import org.lightning323.frikinjay.letmedespawn.config.LetMeDespawnConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.lightning323.performancetweaks.Performancetweaks;
import org.slf4j.Logger;

import java.io.File;

public final class LetMeDespawn {
//    public static final String MOD_ID = "letmedespawn";

    public static final Logger logger = LogUtils.getLogger();
    public static final File CONFIG_FILE = new File("config/"+ Performancetweaks.MOD_ID +"/letmedespawn.json");
    public static LetMeDespawnConfig config;

    public static void init() {
        logger.info("LetMeDespawn init");
        config = LetMeDespawnConfig.load();
        Almanac.addConfigChangeListener(CONFIG_FILE, newConfig -> {
            config = (LetMeDespawnConfig) newConfig;
            logger.info("LetMeDespawn config reloaded");
        });
        config.save();
        Almanac.addCommandRegistration(LetMeDespawnCommands::register);
    }

    public static void setPersistence(Mob entity, EquipmentSlot slot) {
        ItemStack itemStack = entity.getItemBySlot(slot);

        // Vanilla 1.20.1 way to handle NBT in common code
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.putBoolean("picked", true);

        Almanac.pickedItems = true;

        // Use BuiltInRegistries for common registry access in 1.20.1
        String entityKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();

        entity.persistenceRequired = LetMeDespawn.config.getMobNames().contains(entityKey)
                || !hasDespawnableName(entity);
    }

    public static boolean hasDespawnableName(Mob entity) {
        if(entity.hasCustomName()) {
            return Almanac.matchesStackedName(entity.getCustomName().getString(), entity);
        }
        return true;
    }
}
