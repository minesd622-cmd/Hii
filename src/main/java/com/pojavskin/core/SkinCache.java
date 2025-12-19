package com.pojavskin.core;

import com.pojavskin.SkinChangerMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SkinCache {
    private static Path cacheDir;

    public static void init() {
        cacheDir = FabricLoader.getInstance().getGameDir().resolve("cached_skins");
        try {
            if (!Files.exists(cacheDir)) {
                Files.createDirectories(cacheDir);
            }
        } catch (IOException e) {
            SkinChangerMod.LOGGER.error("Could not create skin cache directory", e);
        }
    }

    public static byte[] get(String username) {
        Path skinPath = cacheDir.resolve(username + ".png");
        if (Files.exists(skinPath)) {
            try {
                return Files.readAllBytes(skinPath);
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    public static void save(String username, byte[] data) {
        Path skinPath = cacheDir.resolve(username + ".png");
        try {
            Files.write(skinPath, data);
        } catch (IOException e) {
            SkinChangerMod.LOGGER.error("Failed to cache skin", e);
        }
    }
}