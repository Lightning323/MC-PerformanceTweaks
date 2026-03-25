package org.lightning323.performancetweaks;

import net.minecraftforge.fml.common.Mod;
import org.lightning323.performancetweaks.PerformanceTweaks;
import org.lightning323.performancetweaks.Constants;

@Mod(Constants.MOD_ID)
public class PerformanceTweaksForge {
    
    public PerformanceTweaksForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        PerformanceTweaks.init();
        
    }
}