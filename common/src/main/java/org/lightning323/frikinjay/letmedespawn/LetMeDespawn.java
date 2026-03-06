package org.lightning323.frikinjay.letmedespawn;

import net.minecraft.core.registries.BuiltInRegistries;
import org.lightning323.frikinjay.almanac.Almanac;
import org.lightning323.frikinjay.letmedespawn.command.LetMeDespawnCommands;
import org.lightning323.frikinjay.letmedespawn.config.LetMeDespawnConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.lightning323.perf_tweaks.Perf_tweaks;
import org.slf4j.Logger;

import java.io.File;

import static org.lightning323.perf_tweaks.Perf_tweaks.LET_ME_DESPAWN_CONFIG_FILE;

public final class LetMeDespawn {
    public static final String MOD_ID = Perf_tweaks.MOD_ID;

    public static final Logger logger = LogUtils.getLogger();
    public static final File CONFIG_FILE = LET_ME_DESPAWN_CONFIG_FILE;
    public static LetMeDespawnConfig config;

    public static void init() {
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
        CustomData component = itemStack.get(DataComponents.CUSTOM_DATA);
        CompoundTag nbt;
        if (component != null) {
            nbt = component.copyTag();
        } else {
            nbt = new CompoundTag();
        }
        nbt.putBoolean("picked", true);
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        Almanac.pickedItems = true;

        String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
        entity.persistenceRequired = LetMeDespawn.config.getMobNames().contains(entityId)
                || !hasDespawnableName(entity);
    }

    public static boolean hasDespawnableName(Mob entity) {
        if(entity.hasCustomName()) {
            return Almanac.matchesStackedName(entity.getCustomName().getString(), entity);
        }
        return true;
    }
}
