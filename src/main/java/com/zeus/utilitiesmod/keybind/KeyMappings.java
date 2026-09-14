package com.zeus.utilitiesmod.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {

    public static KeyMapping OPEN_GUI;

    public static void initialize() {
        OPEN_GUI = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.utilitiesmod.open_gui",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_0,
                        KeyMapping.Category.MISC
                )
        );
    }
}