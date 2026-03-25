package org.lightning323.performancetweaks;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import org.lightning323.performancetweaks.config.ConfigManager;
import org.lightning323.performancetweaks.optimizations.server.despawn.DespawnCommandUtils;
import org.lightning323.performancetweaks.optimizations.server.redstone.RedstoneOptimization;
import org.lightning323.performancetweaks.optimizations.server.redstone.command.RedstoneCommandUtils;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(Constants.MOD_ID)
                .requires(source -> source.hasPermission(2)); // Permission check at the top level

        /**
         * Despwaning optimization
         */
        if (ConfigManager.INSTANCE.enableDespawningOptimization) {
            builder.then(literal("persistence")
                            .requires(source -> source.hasPermission(2))
                            .then(literal("add")
                                    .then(argument("mobName", StringArgumentType.greedyString())
                                            .suggests(DespawnCommandUtils::suggestMobNames)
                                            .executes(DespawnCommandUtils::addMob)))
                            .then(literal("remove")
                                    .then(argument("mobName", StringArgumentType.greedyString())
                                            .suggests(DespawnCommandUtils::suggestConfiguredMobNames)
                                            .executes(DespawnCommandUtils::removeMob))));
        }

        /**
         * Redstone optimization
         */
        builder.then(Commands.literal("redstone").
                requires(source -> source.hasPermission(2)).
                executes(context -> RedstoneCommandUtils.queryEnabled(context.getSource())).
                then(Commands.
                        literal("on").
                        executes(context ->
                                RedstoneCommandUtils.setEnabled(context.getSource(), true))).
                then(Commands.
                        literal("off").
                        executes(context ->
                                RedstoneCommandUtils.setEnabled(context.getSource(), false))).
                then(Commands.
                        literal("updateOrder").
                        executes(context ->
                                RedstoneCommandUtils.queryUpdateOrder(context.getSource())).
                        then(Commands.
                                argument("updateOrder", StringArgumentType.word()).
                                suggests((context, suggestionBuilder) ->
                                        SharedSuggestionProvider.suggest(RedstoneCommandUtils.UPDATE_ORDERS, suggestionBuilder)).
                                executes(context ->
                                        RedstoneCommandUtils.setUpdateOrder(context.getSource(),
                                                RedstoneCommandUtils.parseUpdateOrder(context, "updateOrder"))))).
                then(Commands.
                        literal("resetProfiler").
                        requires(source -> RedstoneOptimization.DEBUG).
                        executes(context ->
                                RedstoneCommandUtils.resetProfiler(context.getSource()))));

        dispatcher.register(builder);
    }
}
