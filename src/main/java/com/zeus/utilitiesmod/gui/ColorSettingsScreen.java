package com.zeus.utilitiesmod.gui;

import com.zeus.utilitiesmod.config.ConfigManager;
import com.zeus.utilitiesmod.setting.ColorSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ColorSettingsScreen extends Screen {

    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 250;

    private final ColorSetting colorSetting;
    private final Screen parent;

    private final List<PresetButton> presetButtons = new ArrayList<>();

    private NumericEditBox redInput;
    private NumericEditBox greenInput;
    private NumericEditBox blueInput;

    public ColorSettingsScreen(
            ColorSetting colorSetting,
            Screen parent
    ) {
        super(Component.literal(colorSetting.getName()));

        this.colorSetting = colorSetting;
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        presetButtons.clear();

        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;

        int inputX = panelX + 110;
        int inputWidth = 100;
        int inputHeight = 25;

        redInput = createInput(
                inputX,
                panelY + 45,
                inputWidth,
                inputHeight,
                colorSetting.getRed()
        );

        greenInput = createInput(
                inputX,
                panelY + 75,
                inputWidth,
                inputHeight,
                colorSetting.getGreen()
        );

        blueInput = createInput(
                inputX,
                panelY + 105,
                inputWidth,
                inputHeight,
                colorSetting.getBlue()
        );

        addRenderableWidget(redInput);
        addRenderableWidget(greenInput);
        addRenderableWidget(blueInput);

        int presetWidth = 80;
        int presetHeight = 25;

        int presetStartX = panelX + 20;
        int presetStartY = panelY + 145;

        int columnSpacing = 90;
        int rowSpacing = 30;

        addPreset(
                "White",
                255, 255, 255,
                presetStartX,
                presetStartY
        );

        addPreset(
                "Black",
                0, 0, 0,
                presetStartX + columnSpacing,
                presetStartY
        );

        addPreset(
                "Red",
                255, 0, 0,
                presetStartX + columnSpacing * 2,
                presetStartY
        );

        addPreset(
                "Green",
                0, 200, 80,
                presetStartX,
                presetStartY + rowSpacing
        );

        addPreset(
                "Blue",
                0, 120, 255,
                presetStartX + columnSpacing,
                presetStartY + rowSpacing
        );

        addPreset(
                "Yellow",
                255, 255, 0,
                presetStartX + columnSpacing * 2,
                presetStartY + rowSpacing
        );

        addPreset(
                "Cyan",
                0, 200, 200,
                presetStartX,
                presetStartY + rowSpacing * 2
        );

        addPreset(
                "Purple",
                160, 32, 240,
                presetStartX + columnSpacing,
                presetStartY + rowSpacing * 2
        );

        addPreset(
                "Pink",
                255, 105, 180,
                presetStartX + columnSpacing * 2,
                presetStartY + rowSpacing * 2
        );
    }

    private NumericEditBox createInput(
            int x,
            int y,
            int width,
            int height,
            int value
    ) {
        NumericEditBox input = new NumericEditBox(
                Minecraft.getInstance().font,
                x,
                y,
                width,
                height,
                Component.literal("Color")
        );

        input.setValue(String.valueOf(value));
        input.setMaxLength(3);
        input.setBordered(true);
        input.setTextColor(0xFFFFFFFF);
        input.setTextShadow(false);

        return input;
    }

    private void addPreset(
            String name,
            int red,
            int green,
            int blue,
            int x,
            int y
    ) {
        presetButtons.add(
                new PresetButton(
                        name,
                        red,
                        green,
                        blue,
                        x,
                        y,
                        80,
                        25
                )
        );
    }

    @Override
    public boolean keyPressed(KeyEvent event) {

        if (event.key() == GLFW.GLFW_KEY_ENTER
                || event.key() == GLFW.GLFW_KEY_KP_ENTER) {

            applyColor();
            return true;
        }

        if (event.key() == GLFW.GLFW_KEY_ESCAPE) {

            if (cancelFocusedInput()) {
                return true;
            }

            Minecraft.getInstance().setScreen(parent);
            return true;
        }

        return super.keyPressed(event);
    }

    private void applyColor() {

        int red = parseInput(
                redInput,
                colorSetting.getRed()
        );

        int green = parseInput(
                greenInput,
                colorSetting.getGreen()
        );

        int blue = parseInput(
                blueInput,
                colorSetting.getBlue()
        );

        colorSetting.setRGB(red, green, blue);

        ConfigManager.save();

        Minecraft.getInstance().setScreen(parent);
    }

    private int parseInput(
            EditBox input,
            int fallback
    ) {
        try {
            if (input.getValue().isEmpty()) {
                return fallback;
            }

            return Math.clamp(
                    Integer.parseInt(input.getValue()),
                    0,
                    255
            );

        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private boolean cancelFocusedInput() {

        if (redInput.isFocused()) {
            redInput.setValue(
                    String.valueOf(colorSetting.getRed())
            );
            redInput.setFocused(false);
            return true;
        }

        if (greenInput.isFocused()) {
            greenInput.setValue(
                    String.valueOf(colorSetting.getGreen())
            );
            greenInput.setFocused(false);
            return true;
        }

        if (blueInput.isFocused()) {
            blueInput.setValue(
                    String.valueOf(colorSetting.getBlue())
            );
            blueInput.setFocused(false);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseClicked(
            MouseButtonEvent event,
            boolean doubleClick
    ) {
        for (PresetButton button : presetButtons) {

            if (button.mouseClicked(event, doubleClick)) {

                colorSetting.setRGB(
                        button.red,
                        button.green,
                        button.blue
                );

                ConfigManager.save();

                redInput.setValue(
                        String.valueOf(button.red)
                );

                greenInput.setValue(
                        String.valueOf(button.green)
                );

                blueInput.setValue(
                        String.valueOf(button.blue)
                );

                return true;
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        super.extractRenderState(
                graphics,
                mouseX,
                mouseY,
                delta
        );

        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;

        graphics.fill(
                panelX,
                panelY,
                panelX + PANEL_WIDTH,
                panelY + PANEL_HEIGHT,
                0xF0101010
        );

        String title = "Color";

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

        drawLabel(
                graphics,
                "Red",
                panelX + 40,
                panelY + 53
        );

        drawLabel(
                graphics,
                "Green",
                panelX + 40,
                panelY + 83
        );

        drawLabel(
                graphics,
                "Blue",
                panelX + 40,
                panelY + 113
        );

        // Live preview
        int previewX = panelX + 225;
        int previewY = panelY + 45;

        graphics.fill(
                previewX,
                previewY,
                previewX + 45,
                previewY + 85,
                colorSetting.getValue()
        );

        graphics.text(
                this.font,
                "Presets",
                panelX + 20,
                panelY + 135,
                0xFFB0B0B0,
                false
        );

        for (PresetButton button : presetButtons) {
            button.extractRenderState(
                    graphics,
                    this.font,
                    mouseX,
                    mouseY
            );
        }

        redInput.extractWidgetRenderState(
                graphics,
                mouseX,
                mouseY,
                delta
        );

        greenInput.extractWidgetRenderState(
                graphics,
                mouseX,
                mouseY,
                delta
        );

        blueInput.extractWidgetRenderState(
                graphics,
                mouseX,
                mouseY,
                delta
        );
    }

    private void drawLabel(
            GuiGraphicsExtractor graphics,
            String text,
            int x,
            int y
    ) {
        graphics.text(
                this.font,
                text,
                x,
                y,
                0xFFFFFFFF,
                false
        );
    }

    private static class PresetButton {

        private final String name;

        private final int red;
        private final int green;
        private final int blue;

        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private PresetButton(
                String name,
                int red,
                int green,
                int blue,
                int x,
                int y,
                int width,
                int height
        ) {
            this.name = name;

            this.red = red;
            this.green = green;
            this.blue = blue;

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
                Font font,
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

            int textX =
                    x + (width - font.width(name)) / 2;

            int textY =
                    y + (height - font.lineHeight) / 2;

            graphics.text(
                    font,
                    name,
                    textX,
                    textY,
                    0xFFFFFFFF,
                    false
            );
        }
    }

    private static class NumericEditBox extends EditBox {

        private NumericEditBox(
                Font font,
                int x,
                int y,
                int width,
                int height,
                Component narration
        ) {
            super(
                    font,
                    x,
                    y,
                    width,
                    height,
                    narration
            );
        }

        @Override
        public void insertText(String input) {

            String digitsOnly =
                    input.replaceAll("[^0-9]", "");

            if (!digitsOnly.isEmpty()) {
                super.insertText(digitsOnly);
            }
        }

        @Override
        public boolean mouseClicked(
                MouseButtonEvent event,
                boolean doubleClick
        ) {
            boolean result =
                    super.mouseClicked(
                            event,
                            doubleClick
                    );

            if (result && event.button() == 0) {
                setCursorPosition(0);
                setHighlightPos(getValue().length());
            }

            return result;
        }
    }
}