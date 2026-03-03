package org.lightning323.performancetweaks.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.lightning323.performancetweaks.Performancetweaks;
import org.lightning323.performancetweaks.config.ConfigManager;

import static net.neoforged.fml.loading.FMLPaths.CONFIGDIR;

@Mod(Performancetweaks.MOD_ID)
public final class PerformancetweaksNeoForge {

    static{
        ConfigManager.init(CONFIGDIR.get().toFile());
    }

    public PerformancetweaksNeoForge() {
        //Register event bus with neoforge
        NeoForge.EVENT_BUS.register(this);

        // Run our common setup.
        Performancetweaks.init();
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        Performancetweaks.onRegisterCommands(event.getDispatcher());
    }
}
