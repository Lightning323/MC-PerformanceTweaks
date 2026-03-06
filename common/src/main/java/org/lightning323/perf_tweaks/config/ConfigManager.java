package org.lightning323.perf_tweaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static org.lightning323.perf_tweaks.Perf_tweaks.*;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;
    public static ModConfig INSTANCE;


    public static void load() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                INSTANCE = GSON.fromJson(reader, ModConfig.class);
                LOGGER.info("Loaded config {}", configFile.getAbsolutePath());
            } catch (IOException e) {
                INSTANCE = new ModConfig();
                LOGGER.error("Error loading config {}", configFile.getAbsolutePath());
            }
        } else {
            INSTANCE = new ModConfig();
            save(); // Create the file with defaults
        }
    }

    public static void init(File path) {
        File configDir = new File(path, MOD_ID);
        if (configDir.mkdirs()) {
            LOGGER.info("Made config dir at {}", configDir.getAbsolutePath());
        }
        configFile = new File(configDir, MOD_ID + ".json");
        load();
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(INSTANCE, writer);
            LOGGER.info("Saved config {}", configFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Error saving config {}", configFile.getAbsolutePath());
        }
    }


}