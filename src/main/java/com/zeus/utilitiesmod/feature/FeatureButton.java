package com.zeus.utilitiesmod.feature;

import com.zeus.utilitiesmod.config.ConfigManager;
import com.zeus.utilitiesmod.gui.FeatureSettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class FeatureButton {

    private final Feature feature;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private static final int TEXT_PADDING = 6;

    public FeatureButton(Feature feature, int x, int y, int width, int height) {
        this.feature = feature;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    private String getDisplayName(Font font) {
        String name = feature.getName();
        int maxWidth = width - TEXT_PADDING * 2;

        if (font.width(name) <= maxWidth) {
            return name;
        }

        String ellipsis = "…";
        int ellipsisWidth = font.width(ellipsis);

        StringBuilder shortened = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {
            String next = shortened.toString() + name.charAt(i);

            if (font.width(next) + ellipsisWidth > maxWidth) {
                break;
            }

            shortened.append(name.charAt(i));
        }

        return shortened + ellipsis;
    }

    public Feature getFeature() {
        return feature;
    }

    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            Font font,
            int mouseX,
            int mouseY
    ) {
        boolean hovered =
                mouseX >= x
                        && mouseX <= x + width
                        && mouseY >= y
                        && mouseY <= y + height;

        int buttonColor;

        if (feature.isEnabled()) {
            buttonColor = hovered
                    ? 0xFF438686
                    : 0xFF356B6B;
        } else {
            buttonColor = hovered
                    ? 0xFF505050
                    : 0xFF303030;
        }

        graphics.fill(
                x,
                y,
                x + width,
                y + height,
                buttonColor
        );

        String text = getDisplayName(font);

        int textX = x + (width - font.width(text)) / 2;
        int textY = y + (height - font.lineHeight) / 2;

        graphics.text(
                font,
                text,
                textX,
                textY,
                0xFFFFFFFF,
                false
        );

        if (hovered && !feature.getName().equals(text)) {
            graphics.setTooltipForNextFrame(
                    Component.literal(feature.getName()),
                    mouseX,
                    mouseY
            );
        }
    }

    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();

        if (mouseX >= x
                && mouseX <= x + width
                && mouseY >= y
                && mouseY <= y + height) {

            if (event.button() == 0) {
                feature.toggle();
                ConfigManager.save();
                return true;
            }

            if (event.button() == 1) {
                Minecraft.getInstance().setScreen(
                        new FeatureSettingsScreen(feature, Minecraft.getInstance().screen)
                );
                return true;
            }
        }

        return false;
    }
}