package com.pojavskin.mixin;

import com.pojavskin.config.ConfigManager;
import com.pojavskin.core.SkinLoader;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayerEntity {

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void injectCustomSkin(CallbackInfoReturnable<SkinTextures> cir) {
        if (!ConfigManager.get().enableCustomSkins) return;

        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        String username = player.getGameProfile().getName();

        // Try to get custom skin
        Identifier customSkin = SkinLoader.getInstance().getSkin(username);
        
        if (customSkin != null) {
            // Use standard Steve/Alex model based on UUID hash usually, but here we preserve existing model type
            SkinTextures original = cir.getReturnValue();
            SkinTextures newTextures = new SkinTextures(
                customSkin,
                null, // Cape texture
                null, // Elytra texture
                original.model(),
                true // secure
            );
            cir.setReturnValue(newTextures);
        }
    }
}