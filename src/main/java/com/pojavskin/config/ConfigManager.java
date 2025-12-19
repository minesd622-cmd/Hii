package com.pojavskin.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pojavskin.SkinChangerMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("pojavskinreforged.json").toFile();
    private static ModConfig config;

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                config = GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                SkinChangerMod.LOGGER.error("Failed to load config, using defaults", e);
                config = new ModConfig();
            }
        } else {
            config = new ModConfig();
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            SkinChangerMod.LOGGER.error("Failed to save config", e);
        }
    }

    public static ModConfig get() {
        if (config == null) load();
        return config;
    }
}