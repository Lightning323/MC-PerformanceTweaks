package org.lightning323.performancetweaks;

import org.lightning323.performancetweaks.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class PerformanceTweaks {
    public static void init() {

        if (Services.PLATFORM.isDevelopmentEnvironment())
            Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

    }
}