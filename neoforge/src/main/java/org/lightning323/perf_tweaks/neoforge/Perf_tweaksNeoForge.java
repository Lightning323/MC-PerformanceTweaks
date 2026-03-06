package org.lightning323.perf_tweaks.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.lightning323.perf_tweaks.Perf_tweaks;
import net.neoforged.fml.common.Mod;
import org.lightning323.perf_tweaks.config.ConfigManager;

import static net.neoforged.fml.loading.FMLPaths.CONFIGDIR;

@Mod(Perf_tweaks.MOD_ID)
public final class Perf_tweaksNeoForge {

    static{
        ConfigManager.init(CONFIGDIR.get().toFile());
    }

    public Perf_tweaksNeoForge() {
        //Register event bus with neoforge
        NeoForge.EVENT_BUS.register(this);

        // Run our common setup.
        Perf_tweaks.init();
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        Perf_tweaks.onRegisterCommands(event.getDispatcher());
    }
}
