package org.zipCoder.perf_tweaks.fabric;

import org.zipCoder.perf_tweaks.Perf_tweaks;
import net.fabricmc.api.ModInitializer;

public final class PerformanceTweaksFabric implements ModInitializer {

    static{
        ConfigManager.init(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile());
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Performancetweaks.init();

        //Command registration
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            Performancetweaks.onRegisterCommands(dispatcher);
        });
    }
}
