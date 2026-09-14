package com.zeus.utilitiesmod.feature;

import com.zeus.utilitiesmod.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class Feature {

    private final String name;
    private final String description;
    private boolean enabled;

    private final List<Setting> settings = new ArrayList<>();

    public Feature(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }
}