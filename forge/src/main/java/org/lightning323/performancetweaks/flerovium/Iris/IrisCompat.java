package org.lightning323.performancetweaks.flerovium.Iris;

import com.mojang.blaze3d.vertex.VertexFormat;
import org.lightning323.performancetweaks.platform.Services;

import java.lang.reflect.Field;

public final class IrisCompat {
    private IrisCompat() {
    }

    public static final boolean IS_IRIS_INSTALLED = Services.PLATFORM.isModLoaded("iris");

    static VertexFormat GetEntityVertexFormat() {
        try {
            Class<?> irisVertexFormats = Class.forName("net.irisshaders.iris.vertices.IrisVertexFormats");
            Field field = irisVertexFormats.getDeclaredField("ENTITY");
            return (VertexFormat) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }

    static VertexFormat GetTerrainVertexFormat() {
        try {
            Class<?> irisVertexFormats = Class.forName("net.irisshaders.iris.vertices.IrisVertexFormats");
            Field field = irisVertexFormats.getDeclaredField("TERRAIN");
            return (VertexFormat) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
