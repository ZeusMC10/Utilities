package com.zeus.utilitiesmod.keybind;

import com.zeus.utilitiesmod.gui.UtilitiesScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class KeyInputHandler {

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (KeyMappings.OPEN_GUI.consumeClick()) {
                client.setScreen(new UtilitiesScreen());
            }

        });
    }
}