package com.zeus.utilitiesmod;

import com.zeus.utilitiesmod.command.CommandHandler;
import com.zeus.utilitiesmod.config.ConfigManager;
import com.zeus.utilitiesmod.feature.FeatureManager;
import com.zeus.utilitiesmod.hud.ClockHudElement;
import com.zeus.utilitiesmod.keybind.KeyInputHandler;
import com.zeus.utilitiesmod.keybind.KeyMappings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

public class UtilitiesClient implements ClientModInitializer {

    public static final FeatureManager FEATURE_MANAGER = new FeatureManager();

    @Override
    public void onInitializeClient() {
        ConfigManager.createIfMissing();
        ConfigManager.load();

        KeyMappings.initialize();
        KeyInputHandler.initialize();
        CommandHandler.initialize();

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("utilitiesmod", "clock"),
                new ClockHudElement()
        );
    }
}