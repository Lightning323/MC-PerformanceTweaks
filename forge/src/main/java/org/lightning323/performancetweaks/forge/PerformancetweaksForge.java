package org.lightning323.performancetweaks.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lightning323.alternate.current.command.AlternateCurrentCommand;
import org.lightning323.performancetweaks.Performancetweaks;
import org.lightning323.performancetweaks.config.ConfigManager;

@Mod(Performancetweaks.MOD_ID)
public final class PerformancetweaksForge {
    public PerformancetweaksForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Performancetweaks.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
        // Run our common setup.
        Performancetweaks.init();

        ConfigManager.init(net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR.get().toFile(), Performancetweaks.MOD_ID);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        Performancetweaks.onRegisterCommands(event.getDispatcher());
    }
}
