package com.pojavskin.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pojavskin.config.ConfigManager;
import com.pojavskin.core.SkinLoader;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class SkinCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("skin")
            .then(literal("reload")
                .executes(ctx -> {
                    ConfigManager.load();
                    SkinLoader.getInstance().clearMemoryCache();
                    ctx.getSource().sendFeedback(Text.literal("§aConfig and Skin Cache reloaded!"));
                    return 1;
                }))
            .then(literal("set")
                .then(argument("username", StringArgumentType.string())
                    .executes(ctx -> {
                        String username = StringArgumentType.getString(ctx, "username");
                        ctx.getSource().sendFeedback(Text.literal("§eRefreshing skin for: " + username + " (requires rejoin/respawn to visualize)"));
                        SkinLoader.getInstance().forceUpdate(username);
                        return 1;
                    }))));
    }
}