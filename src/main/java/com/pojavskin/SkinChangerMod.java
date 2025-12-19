package com.pojavskin;

import com.pojavskin.command.SkinCommand;
import com.pojavskin.config.ConfigManager;
import com.pojavskin.core.SkinCache;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkinChangerMod implements ClientModInitializer {
    public static final String MOD_ID = "pojavskinreforged";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Pojav Skin Reforged...");
        
        // Initialize Subsystems
        ConfigManager.load();
        SkinCache.init();
        
        // Register Commands
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            SkinCommand.register(dispatcher);
        });

        LOGGER.info("Initialization Complete.");
    }
}