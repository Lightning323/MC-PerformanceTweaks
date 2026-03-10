package org.lightning323.performancetweaks.fabric;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.lightning323.performancetweaks.Performancetweaks;
import net.fabricmc.api.ModInitializer;

public final class PerformancetweaksFabric implements ModInitializer {



    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Performancetweaks.init(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile());

        //Command registration
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            Performancetweaks.onRegisterCommands(dispatcher);
        });
    }
}
