package com.zeus.utilitiesmod.setting;

import com.zeus.utilitiesmod.config.ConfigManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;

public class SettingButton {

    private final Setting setting;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public SettingButton(
            Setting setting,
            int x,
            int y,
            int width,
            int height
    ) {
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

        int backgroundColor;

        if (setting instanceof BooleanSetting booleanSetting
                && booleanSetting.getValue()) {
            backgroundColor = hovered
                    ? 0xFF5AA6A6
                    : 0xFF4A8F8F;
        } else {
            backgroundColor = hovered
                    ? 0xFF505050
                    : 0xFF303030;
        }

        graphics.fill(
                x,
                y,
                x + width,
                y + height,
                backgroundColor
        );

        String text = getDisplayText();

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
    }

    private String getDisplayText() {
        if (setting instanceof ModeSetting modeSetting) {
            return modeSetting.getName()
                    + ": "
                    + modeSetting.getValue();
        }

        if (setting instanceof IntegerSetting integerSetting) {
            return integerSetting.getName()
                    + ": "
                    + integerSetting.getValue();
        }

        return setting.getName();
    }



    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();

        if (mouseX >= x
                && mouseX <= x + width
                && mouseY >= y
                && mouseY <= y + height) {

            if (setting instanceof ModeSetting modeSetting
                    && event.button() == 0) {
                modeSetting.cycle();
                ConfigManager.save();
                return true;
            }

            if (setting instanceof BooleanSetting booleanSetting
                    && event.button() == 0) {
                booleanSetting.toggle();
                ConfigManager.save();
                return true;
            }

            if (setting instanceof IntegerSetting integerSetting) {
                if (event.button() == 0) {
                    integerSetting.setValue(
                            integerSetting.getValue() + 1
                    );
                    return true;
                }

                if (event.button() == 1) {
                    integerSetting.setValue(
                            integerSetting.getValue() - 1
                    );
                    return true;
                }
            }

            return true;
        }

        return false;
    }
}