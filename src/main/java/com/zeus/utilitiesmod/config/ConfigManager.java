package com.zeus.utilitiesmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zeus.utilitiesmod.UtilitiesClient;
import com.zeus.utilitiesmod.feature.Feature;
import com.zeus.utilitiesmod.setting.BooleanSetting;
import com.zeus.utilitiesmod.setting.ColorSetting;
import com.zeus.utilitiesmod.setting.IntegerSetting;
import com.zeus.utilitiesmod.setting.ModeSetting;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigManager {

    private static final String FILE_NAME = "utilitiesmod.json";

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Path getConfigPath() {
        return Minecraft.getInstance()
                .gameDirectory
                .toPath()
                .resolve("config")
                .resolve(FILE_NAME);
    }

    public static void createIfMissing() {
        Path configPath = getConfigPath();

        try {
            Files.createDirectories(configPath.getParent());

            if (Files.notExists(configPath)) {
                Files.createFile(configPath);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void save() {
        createIfMissing();

        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> features = new LinkedHashMap<>();

        for (Feature feature : UtilitiesClient.FEATURE_MANAGER.getFeatures()) {
            Map<String, Object> featureData = new LinkedHashMap<>();

            featureData.put("enabled", feature.isEnabled());

            for (var setting : feature.getSettings()) {

                if (setting instanceof BooleanSetting booleanSetting) {
                    featureData.put(
                            setting.getName(),
                            booleanSetting.getValue()
                    );
                }

                if (setting instanceof ModeSetting modeSetting) {
                    featureData.put(
                            setting.getName(),
                            modeSetting.getValue()
                    );
                }

                if (setting instanceof IntegerSetting integerSetting) {
                    featureData.put(
                            setting.getName(),
                            integerSetting.getValue()
                    );
                }

                if (setting instanceof ColorSetting colorSetting) {
                    featureData.put(
                            setting.getName(),
                            colorSetting.getValue()
                    );
                }
            }

            features.put(feature.getName(), featureData);
        }

        root.put("features", features);

        try {
            Files.writeString(
                    getConfigPath(),
                    GSON.toJson(root)
            );

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void load() {
        Path configPath = getConfigPath();

        if (Files.notExists(configPath)) {
            return;
        }

        try {
            String json = Files.readString(configPath);

            if (json.isBlank()) {
                return;
            }

            Map<?, ?> root = GSON.fromJson(json, Map.class);

            if (root == null) {
                return;
            }

            Object featuresObject = root.get("features");

            if (!(featuresObject instanceof Map<?, ?> features)) {
                return;
            }

            for (Feature feature : UtilitiesClient.FEATURE_MANAGER.getFeatures()) {

                Object featureObject = features.get(feature.getName());

                if (!(featureObject instanceof Map<?, ?> featureData)) {
                    continue;
                }

                Object enabledObject = featureData.get("enabled");

                if (enabledObject instanceof Boolean enabled) {
                    feature.setEnabled(enabled);
                }

                for (var setting : feature.getSettings()) {

                    if (setting instanceof BooleanSetting booleanSetting) {
                        Object valueObject = featureData.get(setting.getName());

                        if (valueObject instanceof Boolean value) {
                            booleanSetting.setValue(value);
                        }
                    }

                    if (setting instanceof ModeSetting modeSetting) {
                        Object valueObject = featureData.get(setting.getName());

                        if (valueObject instanceof String value) {
                            modeSetting.setValue(value);
                        }
                    }

                    if (setting instanceof IntegerSetting integerSetting) {
                        Object valueObject = featureData.get(setting.getName());

                        if (valueObject instanceof Number value) {
                            integerSetting.setValue(value.intValue());
                        }
                    }

                    if (setting instanceof ColorSetting colorSetting) {
                        Object valueObject = featureData.get(setting.getName());

                        if (valueObject instanceof Number value) {
                            colorSetting.setValue(value.intValue());
                        }
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}