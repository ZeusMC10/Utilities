package com.zeus.utilitiesmod.gui;

import com.zeus.utilitiesmod.UtilitiesClient;
import com.zeus.utilitiesmod.feature.Feature;
import com.zeus.utilitiesmod.feature.FeatureButton;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.input.MouseButtonEvent;

import java.util.ArrayList;
import java.util.List;

public class UtilitiesScreen extends Screen {

    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 160;

    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 25;
    private static final int BUTTON_SPACING = 10;

    private static final int PANEL_PADDING = 20;
    private static final int CONTENT_TOP = 50;

    private static final int GRID_COLUMNS = 3;

    private final List<FeatureButton> featureButtons = new ArrayList<>();

    public UtilitiesScreen() {
        super(Component.literal("Utilities"));
    }

    @Override
    protected void init() {
        super.init();

        featureButtons.clear();

        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;

        List<Feature> features = UtilitiesClient.FEATURE_MANAGER.getFeatures();

        for (int i = 0; i < features.size(); i++) {
            Feature feature = features.get(i);

            int column = i % GRID_COLUMNS;
            int row = i / GRID_COLUMNS;

            int buttonX = panelX
                    + PANEL_PADDING
                    + column * (BUTTON_WIDTH + BUTTON_SPACING);

            int buttonY = panelY
                    + CONTENT_TOP
                    + row * (BUTTON_HEIGHT + BUTTON_SPACING);

            featureButtons.add(
                    new FeatureButton(
                            feature,
                            buttonX,
                            buttonY,
                            BUTTON_WIDTH,
                            BUTTON_HEIGHT
                    )
            );
        }
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

        graphics.text(
                this.font,
                "Utilities",
                panelX + (PANEL_WIDTH - this.font.width("Utilities")) / 2,
                panelY + 15,
                0xFFFFFFFF,
                true
        );

        for (FeatureButton featureButton : featureButtons) {
            featureButton.extractRenderState(
                    graphics,
                    this.font,
                    mouseX,
                    mouseY
            );
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        for (FeatureButton featureButton : featureButtons) {
            if (featureButton.mouseClicked(event, doubleClick)) {
                return true;
            }
        }

        return super.mouseClicked(event, doubleClick);
    }
}