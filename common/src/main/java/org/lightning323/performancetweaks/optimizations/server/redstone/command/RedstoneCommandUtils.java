package org.lightning323.performancetweaks.optimizations.server.redstone.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import org.lightning323.performancetweaks.optimizations.server.redstone.RedstoneOptimization;
import org.lightning323.performancetweaks.optimizations.server.redstone.interfaces.mixin.IServerLevel;
import org.lightning323.performancetweaks.optimizations.server.redstone.util.profiler.ProfilerResults;
import org.lightning323.performancetweaks.optimizations.server.redstone.wire.UpdateOrder;
import org.lightning323.performancetweaks.optimizations.server.redstone.wire.WireHandler;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class RedstoneCommandUtils {

    private static final DynamicCommandExceptionType NO_SUCH_UPDATE_ORDER = new DynamicCommandExceptionType(value -> Component.literal("no such update order: " + value));

    public static final String[] UPDATE_ORDERS;

    static {
        UpdateOrder[] updateOrders = UpdateOrder.values();
        UPDATE_ORDERS = new String[updateOrders.length];

        for (int i = 0; i < updateOrders.length; i++) {
            UPDATE_ORDERS[i] = updateOrders[i].id();
        }
    }



    public static UpdateOrder parseUpdateOrder(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        String value = StringArgumentType.getString(context, name);

        try {
            return UpdateOrder.byId(value);
        } catch (Exception e) {
            throw NO_SUCH_UPDATE_ORDER.create(name);
        }
    }

    public static int queryEnabled(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        WireHandler wireHandler = ((IServerLevel) level).alternate_current$getWireHandler();

        String state = wireHandler.getConfig().getEnabled() ? "enabled" : "disabled";
        source.sendSuccess(() -> Component.literal(String.format("%s is currently %s", RedstoneOptimization.MOD_NAME, state)), false);

        return Command.SINGLE_SUCCESS;
    }

    public static int setEnabled(CommandSourceStack source, boolean on) {
        ServerLevel level = source.getLevel();
        WireHandler wireHandler = ((IServerLevel) level).alternate_current$getWireHandler();

        wireHandler.getConfig().setEnabled(on);

        String state = wireHandler.getConfig().getEnabled() ? "enabled" : "disabled";
        source.sendSuccess(() -> Component.literal(String.format("%s has been %s!", RedstoneOptimization.MOD_NAME, state)), true);

        return Command.SINGLE_SUCCESS;
    }

    public static int queryUpdateOrder(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        WireHandler wireHandler = ((IServerLevel) level).alternate_current$getWireHandler();

        String value = wireHandler.getConfig().getUpdateOrder().id();
        source.sendSuccess(() -> Component.literal(String.format("Update order is currently %s", value)), false);

        return Command.SINGLE_SUCCESS;
    }

    public static int setUpdateOrder(CommandSourceStack source, UpdateOrder updateOrder) {
        ServerLevel level = source.getLevel();
        WireHandler wireHandler = ((IServerLevel) level).alternate_current$getWireHandler();

        wireHandler.getConfig().setUpdateOrder(updateOrder);

        String value = wireHandler.getConfig().getUpdateOrder().id();
        source.sendSuccess(() -> Component.literal(String.format("update order has been set to %s!", value)), true);

        return Command.SINGLE_SUCCESS;
    }

    public static int resetProfiler(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("profiler results have been cleared!"), true);

        ProfilerResults.log();
        ProfilerResults.clear();

        return Command.SINGLE_SUCCESS;
    }
}
