package org.lightning323.performancetweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.lightning323.performancetweaks.PerformanceTweaks;
import org.lightning323.performancetweaks.Constants;

public class PerformanceTweaksFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        PerformanceTweaks.init();

        //Command registration
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            PerformanceTweaks.onRegisterCommands(dispatcher);
        });
    }
}
