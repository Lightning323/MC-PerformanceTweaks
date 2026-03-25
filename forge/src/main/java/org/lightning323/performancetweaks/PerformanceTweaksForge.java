package org.lightning323.performancetweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lightning323.performancetweaks.PerformanceTweaks;
import org.lightning323.performancetweaks.Constants;

@Mod(Constants.MOD_ID)
public class PerformanceTweaksForge {
    
    public PerformanceTweaksForge() {
        PerformanceTweaks.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        PerformanceTweaks.onRegisterCommands(event.getDispatcher());
    }
}