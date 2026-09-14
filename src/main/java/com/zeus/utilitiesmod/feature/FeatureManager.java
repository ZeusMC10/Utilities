package com.zeus.utilitiesmod.feature;

import com.zeus.utilitiesmod.setting.ModeSetting;

import java.util.ArrayList;
import java.util.List;

public class FeatureManager {

    private final List<Feature> features = new ArrayList<>();

    public FeatureManager() {
        registerFeatures();
    }

    private void registerFeatures() {
        features.add(new SelfNametagFeature());
        features.add(new MentionPingFeature());
        features.add(new ClockFeature());
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public Feature getFeature(String name) {
        for (Feature feature : features) {
            if (feature.getName().equalsIgnoreCase(name)) {
                return feature;
            }
        }

        return null;
    }
}