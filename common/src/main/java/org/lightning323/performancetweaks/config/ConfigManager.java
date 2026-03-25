package org.lightning323.performancetweaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.lightning323.performancetweaks.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;
    public static PerfTweaksConfig INSTANCE;

    public static void init(File path) {
        File configDir = new File(path, Constants.MOD_ID);
        if (configDir.mkdirs()) {
            Constants.LOG.info("Created config directory: {}", configDir.getAbsolutePath());
        }
        configFile = new File(configDir, Constants.MOD_ID + ".json");
        loadConfig();
    }

    public static void loadConfig() {
        if (configFile.exists()) {
            //Load the config
            try (FileReader reader = new FileReader(configFile)) {
                INSTANCE = GSON.fromJson(reader, PerfTweaksConfig.class);
            } catch (Exception e) {
                Constants.LOG.error("Failed to load config file: {}", configFile.getAbsolutePath(), e);
            }

            //Verify and determine if we need to save the config
            try (FileReader reader = new FileReader(configFile)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                int jsonKeyCount = json.size();
                int classFieldCount = PerfTweaksConfig.class.getDeclaredFields().length;

                Constants.LOG.info("Config loaded; Read JSON entries: {}; Class entries: {}", jsonKeyCount, classFieldCount);
                if (classFieldCount != jsonKeyCount) {
                    Constants.LOG.info("Saving config due to mismatched JSON keys...");
                    saveConfig();
                }
            } catch (Exception e) {
                Constants.LOG.error("Failed to verify config", e);
                saveConfig();
            }
        }

        if (INSTANCE == null) {
            INSTANCE = new PerfTweaksConfig();
            saveConfig(); // Create the file with defaults
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            Constants.LOG.error("Failed to save config", e);
        }
    }
}