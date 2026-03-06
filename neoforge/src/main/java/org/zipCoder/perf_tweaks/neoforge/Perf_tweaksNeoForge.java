package org.zipCoder.perf_tweaks.neoforge;

import org.zipCoder.perf_tweaks.Perf_tweaks;
import net.neoforged.fml.common.Mod;

@Mod(Perf_tweaks.MOD_ID)
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
