package com.zeus.utilitiesmod.hud;

import com.zeus.utilitiesmod.feature.ClockFeature;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClockHudElement implements HudElement {

    @Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {
        ClockFeature clock =
                (ClockFeature) com.zeus.utilitiesmod.UtilitiesClient.FEATURE_MANAGER
                        .getFeature("Clock");

        if (clock == null || !clock.isEnabled()) {
            return;
        }

        LocalTime time = LocalTime.now();

        String pattern = clock.getFormat().getValue().equals("12 Hour")
                ? "hh:mm a"
                : "HH:mm";

        String text = time.format(
                DateTimeFormatter.ofPattern(pattern)
        );

        Minecraft minecraft = Minecraft.getInstance();

        graphics.text(
                minecraft.font,
                text,
                clock.getX().getValue(),
                clock.getY().getValue(),
                clock.getColor().getValue(),
                true
        );
    }
}