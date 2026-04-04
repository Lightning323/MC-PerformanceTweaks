package org.lightning323.performancetweaks.optimizations.despawn;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.MobCategory;
import org.lightning323.performancetweaks.config.ConfigManager;

import java.util.concurrent.CompletableFuture;

public class DespawnCommandUtils {
    public static CompletableFuture<Suggestions> suggestMobNames(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        BuiltInRegistries.ENTITY_TYPE.forEach(entityType -> {
            if (entityType.getCategory().equals(MobCategory.MONSTER)) {
                builder.suggest(BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString());
            }
        });
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestConfiguredMobNames(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ConfigManager.INSTANCE.persistentMobs().stream()
                .filter(mobName -> mobName.startsWith(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static int addMob(CommandContext<CommandSourceStack> context) {
        String mobName = StringArgumentType.getString(context, "mobName");
        if (ConfigManager.INSTANCE.persistentMobs().contains(mobName)) {
            context.getSource().sendSuccess(() -> Component.literal("Mob '" + mobName + "' is already in the configuration.").withStyle(ChatFormatting.RED), false);
        } else {
            ConfigManager.INSTANCE.addMobName(mobName);
            context.getSource().sendSuccess(() -> Component.literal("Added '" + mobName + "' to despawn configuration.").withStyle(ChatFormatting.AQUA), true);
        }
        return 1;
    }

    public static int removeMob(CommandContext<CommandSourceStack> context) {
        String mobName = StringArgumentType.getString(context, "mobName");
        if (ConfigManager.INSTANCE.persistentMobs().contains(mobName)) {
            ConfigManager.INSTANCE.removeMobName(mobName);
            context.getSource().sendSuccess(() -> Component.literal("Removed '" + mobName + "' from despawn configuration.").withStyle(ChatFormatting.GOLD), true);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Mob '" + mobName + "' is not in the configuration.").withStyle(ChatFormatting.RED), false);
        }
        return 1;
    }
}