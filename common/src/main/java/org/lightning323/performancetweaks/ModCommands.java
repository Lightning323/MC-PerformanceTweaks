package org.lightning323.performancetweaks;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import org.lightning323.performancetweaks.config.ConfigManager;
import org.lightning323.performancetweaks.optimizations.server.despawn.DespawnCommandUtils;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        if (ConfigManager.INSTANCE.enableLetMeDespawn) {
            dispatcher.register(literal(Performancetweaks.MOD_ID)
                    .then(literal("persistence")
                            .requires(source -> source.hasPermission(2))
                            .then(literal("add")
                                    .then(argument("mobName", StringArgumentType.greedyString())
                                            .suggests(DespawnCommandUtils::suggestMobNames)
                                            .executes(DespawnCommandUtils::addMob)))
                            .then(literal("remove")
                                    .then(argument("mobName", StringArgumentType.greedyString())
                                            .suggests(DespawnCommandUtils::suggestConfiguredMobNames)
                                            .executes(DespawnCommandUtils::removeMob)))));
        }
    }
}
