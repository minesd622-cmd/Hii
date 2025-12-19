package com.pojavskin.mixin;

import com.mojang.authlib.GameProfile;
import com.pojavskin.config.ConfigManager;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.concurrent.CompletableFuture;

@Mixin(PlayerSkinProvider.class)
public class MixinPlayerSkinProvider {
    // This mixin ensures we don't block main thread if Mojang servers are slow
    // and allows us to hook into fetching logic if necessary.
    
    @Inject(method = "fetchSkinTextures", at = @At("HEAD"))
    private void onFetchSkinTextures(GameProfile profile, CallbackInfoReturnable<CompletableFuture<SkinTextures>> cir) {
        // Just logging or debug logic here if needed
        // The actual replacement happens in AbstractClientPlayerEntity to handle dynamic updates better
        // without complex future manipulation.
    }
}