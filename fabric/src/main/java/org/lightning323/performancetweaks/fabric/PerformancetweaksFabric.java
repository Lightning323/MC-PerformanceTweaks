package org.lightning323.performancetweaks.fabric;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.lightning323.alternate.current.command.AlternateCurrentCommand;
import org.lightning323.performancetweaks.Performancetweaks;
import net.fabricmc.api.ModInitializer;
import org.lightning323.performancetweaks.config.ConfigManager;

public final class PerformancetweaksFabric implements ModInitializer {

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
