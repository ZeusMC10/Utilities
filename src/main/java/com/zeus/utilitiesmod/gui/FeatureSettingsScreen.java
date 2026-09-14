package com.zeus.utilitiesmod.gui;

import com.zeus.utilitiesmod.config.ConfigManager;
import com.zeus.utilitiesmod.feature.Feature;
import com.zeus.utilitiesmod.setting.ColorSetting;
import com.zeus.utilitiesmod.setting.IntegerSetting;
import com.zeus.utilitiesmod.setting.Setting;
import com.zeus.utilitiesmod.setting.SettingButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class FeatureSettingsScreen extends Screen {

    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 160;

    private final Feature feature;
    private final Screen parent;

    private final List<SettingButton> settingButtons = new ArrayList<>();
    private final List<IntegerInput> integerInputs = new ArrayList<>();
    private final List<ColorButton> colorButtons = new ArrayList<>();

    public FeatureSettingsScreen(Feature feature, Screen parent) {
        super(Component.literal(feature.getName()));

        this.feature = feature;
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        settingButtons.clear();
        integerInputs.clear();
        colorButtons.clear();

        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;

        int buttonWidth = 260;
        int buttonHeight = 25;
        int buttonSpacing = 5;

        String description = feature.getDescription();

        List<FormattedCharSequence> descriptionLines =
                this.font.split(
                        Component.literal(description),
                        buttonWidth
                );

        int buttonY =
                panelY
                        + 35
                        + (descriptionLines.size() * this.font.lineHeight)
                        + 10;

        for (Setting setting : feature.getSettings()) {

            if (setting instanceof IntegerSetting integerSetting) {

                int inputX;
                boolean isX =
                        integerSetting.getName().equalsIgnoreCase("X");

                boolean isY =
                        integerSetting.getName().equalsIgnoreCase("Y");

                if (isX) {
                    inputX = panelX + 70;
                } else if (isY) {
                    inputX = panelX + 190;
                } else {
                    inputX = panelX + 160;
                }

                IntegerInput input = new IntegerInput(
                        integerSetting,
                        inputX,
                        buttonY,
                        isX || isY ? 80 : 130,
                        buttonHeight
                );

                integerInputs.add(input);
                addRenderableWidget(input.editBox);

                if (!isX) {
                    buttonY += buttonHeight + buttonSpacing;
                }

            } else if (setting instanceof ColorSetting colorSetting) {

                colorButtons.add(
                        new ColorButton(
                                colorSetting,
                                panelX + 20,
                                buttonY,
                                buttonWidth,
                                buttonHeight
                        )
                );

                buttonY += buttonHeight + buttonSpacing;

            } else {

                settingButtons.add(
                        new SettingButton(
                                setting,
                                panelX + 20,
                                buttonY,
                                buttonWidth,
                                buttonHeight
                        )
                );

                buttonY += buttonHeight + buttonSpacing;
            }
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {

        if (event.key() == GLFW.GLFW_KEY_ENTER
                || event.key() == GLFW.GLFW_KEY_KP_ENTER) {

            if (commitIntegerInputs()) {
                return true;
            }
        }

        if (event.key() == GLFW.GLFW_KEY_ESCAPE) {

            if (cancelFocusedIntegerInput()) {
                return true;
            }

            Minecraft.getInstance().setScreen(parent);
            return true;
        }

        return super.keyPressed(event);
    }

    private boolean commitIntegerInputs() {
        boolean committed = false;

        for (IntegerInput input : integerInputs) {
            if (input.editBox.isFocused()) {

                String value = input.editBox.getValue();

                if (!value.isEmpty()) {
                    input.setting.setValueFromString(value);
                    ConfigManager.save();
                } else {
                    input.editBox.setValue(
                            String.valueOf(input.setting.getValue())
                    );
                }

                input.editBox.setFocused(false);
                input.editBox.setValue(
                        String.valueOf(input.setting.getValue())
                );

                committed = true;
            }
        }

        return committed;
    }

    private boolean cancelFocusedIntegerInput() {
        for (IntegerInput input : integerInputs) {
            if (input.editBox.isFocused()) {

                input.editBox.setValue(
                        String.valueOf(input.setting.getValue())
                );

                input.editBox.setFocused(false);

                return true;
            }
        }

        return false;
    }

    @Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;

        graphics.fill(
                panelX,
                panelY,
                panelX + PANEL_WIDTH,
                panelY + PANEL_HEIGHT,
                0xF0101010
        );

        String title = feature.getName();

        int titleX =
                panelX
                        + (PANEL_WIDTH - this.font.width(title)) / 2;

        graphics.text(
                this.font,
                title,
                titleX,
                panelY + 15,
                0xFFFFFFFF,
                true
        );

        String description = feature.getDescription();

        List<FormattedCharSequence> descriptionLines =
                this.font.split(
                        Component.literal(description),
                        PANEL_WIDTH - 40
                );

        int descriptionY = panelY + 35;

        for (FormattedCharSequence line : descriptionLines) {

            int descriptionX =
                    panelX
                            + (PANEL_WIDTH - this.font.width(line)) / 2;

            graphics.text(
                    this.font,
                    line,
                    descriptionX,
                    descriptionY,
                    0xFFB0B0B0,
                    false
            );

            descriptionY += this.font.lineHeight;
        }

        for (SettingButton settingButton : settingButtons) {
            settingButton.extractRenderState(
                    graphics,
                    this.font,
                    mouseX,
                    mouseY
            );
        }

        for (ColorButton colorButton : colorButtons) {
            colorButton.extractRenderState(
                    graphics,
                    this.font,
                    mouseX,
                    mouseY
            );
        }

        for (IntegerInput input : integerInputs) {

            int labelX;

            if (input.setting.getName().equalsIgnoreCase("X")) {
                labelX = panelX + 30;
            } else if (input.setting.getName().equalsIgnoreCase("Y")) {
                labelX = panelX + 170;
            } else {
                labelX = panelX + 30;
            }

            int labelY =
                    input.y
                            + (input.height - this.font.lineHeight) / 2;

            graphics.text(
                    this.font,
                    input.setting.getName(),
                    labelX,
                    labelY,
                    0xFFFFFFFF,
                    false
            );

            input.editBox.extractWidgetRenderState(
                    graphics,
                    mouseX,
                    mouseY,
                    delta
            );
        }
    }

    @Override
    public boolean mouseClicked(
            MouseButtonEvent event,
            boolean doubleClick
    ) {
        for (SettingButton settingButton : settingButtons) {

            if (settingButton.mouseClicked(event, doubleClick)) {
                return true;
            }
        }

        for (ColorButton colorButton : colorButtons) {

            if (colorButton.mouseClicked(event, doubleClick)) {

                Minecraft.getInstance().setScreen(
                        new ColorSettingsScreen(
                                colorButton.setting,
                                this
                        )
                );

                return true;
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    private static class ColorButton {

        private final ColorSetting setting;

        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private ColorButton(
                ColorSetting setting,
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

        private boolean mouseClicked(
                MouseButtonEvent event,
                boolean doubleClick
        ) {
            if (event.button() != 0) {
                return false;
            }

            return event.x() >= x
                    && event.x() <= x + width
                    && event.y() >= y
                    && event.y() <= y + height;
        }

        private void extractRenderState(
                GuiGraphicsExtractor graphics,
                net.minecraft.client.gui.Font font,
                int mouseX,
                int mouseY
        ) {
            boolean hovered =
                    mouseX >= x
                            && mouseX <= x + width
                            && mouseY >= y
                            && mouseY <= y + height;

            int background = hovered
                    ? 0xFF555555
                    : 0xFF333333;

            graphics.fill(
                    x,
                    y,
                    x + width,
                    y + height,
                    background
            );

            graphics.text(
                    font,
                    setting.getName(),
                    x + 10,
                    y + (height - font.lineHeight) / 2,
                    0xFFFFFFFF,
                    false
            );

            int previewSize = height - 8;

            graphics.fill(
                    x + width - previewSize - 4,
                    y + 4,
                    x + width - 4,
                    y + height - 4,
                    setting.getValue()
            );
        }
    }

    private static class IntegerInput {

        private final IntegerSetting setting;

        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private final NumericEditBox editBox;

        private IntegerInput(
                IntegerSetting setting,
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

            this.editBox = new NumericEditBox(
                    Minecraft.getInstance().font,
                    x,
                    y,
                    width,
                    height,
                    Component.literal(setting.getName())
            );

            this.editBox.setValue(
                    String.valueOf(setting.getValue())
            );

            this.editBox.setMaxLength(10);
            this.editBox.setBordered(true);
            this.editBox.setTextColor(0xFFFFFFFF);
            this.editBox.setTextShadow(false);
        }
    }

    private static class NumericEditBox extends EditBox {

        private NumericEditBox(
                net.minecraft.client.gui.Font font,
                int x,
                int y,
                int width,
                int height,
                Component narration
        ) {
            super(font, x, y, width, height, narration);
        }

        @Override
        public void insertText(String input) {
            String digitsOnly = input.replaceAll("[^0-9]", "");

            if (!digitsOnly.isEmpty()) {
                super.insertText(digitsOnly);
            }
        }

        @Override
        public boolean mouseClicked(
                MouseButtonEvent event,
                boolean doubleClick
        ) {
            boolean result = super.mouseClicked(event, doubleClick);

            if (result && event.button() == 0) {
                setCursorPosition(0);
                setHighlightPos(getValue().length());
            }

            return result;
        }
    }
}