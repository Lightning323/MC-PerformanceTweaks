package org.lightning323.performancetweaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.lightning323.performancetweaks.Performancetweaks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;
    public static ModConfig INSTANCE;

    public static void init(File path) {
        File configDir = new File(path, Performancetweaks.MOD_ID);
        if (configDir.mkdirs()) {
            Performancetweaks.LOGGER.info("Created config directory: " + configDir.getAbsolutePath());
        }
        configFile = new File(configDir, Performancetweaks.MOD_ID + ".json");
        load();
    }

    public static void load() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                INSTANCE = GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                INSTANCE = new ModConfig();
            }
        } else {
            INSTANCE = new ModConfig();
            save(); // Create the file with defaults
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}