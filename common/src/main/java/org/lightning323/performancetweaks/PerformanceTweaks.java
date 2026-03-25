package org.lightning323.performancetweaks;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.lightning323.performancetweaks.config.ConfigManager;
import org.lightning323.performancetweaks.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

import java.io.File;

public class PerformanceTweaks {
    public static void init() {
        if (Services.PLATFORM.isDevelopmentEnvironment())
            Constants.LOG.debug("Performance Tweaks {} in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

        ConfigManager.init(Services.PLATFORM.getConfigDir());
    }

    //common command registration
    public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        Constants.LOG.debug("Registering commands");
        ModCommands.register(dispatcher);
    }
}