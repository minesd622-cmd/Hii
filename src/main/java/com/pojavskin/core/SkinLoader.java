package com.pojavskin.core;

import com.pojavskin.SkinChangerMod;
import com.pojavskin.config.ConfigManager;
import com.pojavskin.util.HttpUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkinLoader {
    private static final SkinLoader INSTANCE = new SkinLoader();
    private final Map<String, Identifier> loadedSkins = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static SkinLoader getInstance() { return INSTANCE; }

    public Identifier getSkin(String username) {
        if (loadedSkins.containsKey(username)) {
            return loadedSkins.get(username);
        }

        loadSkinAsync(username);
        return null; // Return null to let default handling take over temporarily
    }

    public void forceUpdate(String username) {
        loadedSkins.remove(username);
        loadSkinAsync(username);
    }

    public void clearMemoryCache() {
        loadedSkins.clear();
    }

    private void loadSkinAsync(String username) {
        if (loadedSkins.containsKey(username)) return;
        // Put a placeholder to prevent spamming threads
        loadedSkins.put(username, new Identifier("minecraft", "textures/entity/steve.png")); 

        executor.submit(() -> {
            try {
                byte[] skinData = SkinCache.get(username);
                if (skinData == null) {
                    String url = String.format(ConfigManager.get().skinApiUrl, username);
                    skinData = HttpUtil.download(url);
                    if (skinData != null && ConfigManager.get().cacheSkins) {
                        SkinCache.save(username, skinData);
                    }
                }

                if (skinData != null) {
                    final byte[] finalData = skinData;
                    MinecraftClient.getInstance().execute(() -> uploadTexture(username, finalData));
                }
            } catch (Exception e) {
                if (ConfigManager.get().debugMode) {
                    SkinChangerMod.LOGGER.error("Failed to load skin for " + username, e);
                }
            }
        });
    }

    private void uploadTexture(String username, byte[] data) {
        try {
            NativeImage image = NativeImage.read(new ByteArrayInputStream(data));
            NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
            Identifier id = Identifier.of(SkinChangerMod.MOD_ID, "skins/" + username.toLowerCase());
            MinecraftClient.getInstance().getTextureManager().registerTexture(id, texture);
            loadedSkins.put(username, id);
            SkinChangerMod.LOGGER.info("Skin loaded for: " + username);
        } catch (Exception e) {
            SkinChangerMod.LOGGER.error("Texture upload failed", e);
        }
    }
}