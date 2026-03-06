package org.lightning323.perf_tweaks.fabric;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.lightning323.perf_tweaks.Perf_tweaks;
import net.fabricmc.api.ModInitializer;
import org.lightning323.perf_tweaks.config.ConfigManager;

public final class Perf_tweaksFabric implements ModInitializer {

    static{
        ConfigManager.init(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile());
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Perf_tweaks.init();

        //Command registration
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            Perf_tweaks.onRegisterCommands(dispatcher);
        });
    }
}
