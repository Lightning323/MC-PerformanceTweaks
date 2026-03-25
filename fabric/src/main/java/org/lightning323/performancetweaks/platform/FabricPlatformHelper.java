package org.lightning323.performancetweaks.platform;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public File getConfigDir() {
        return net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile();
    }
}
