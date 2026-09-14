package com.zeus.utilitiesmod.setting;

import java.util.List;

public class ModeSetting extends Setting {

    private final List<String> modes;
    private int selectedIndex;

    public ModeSetting(String name, List<String> modes, String defaultMode) {
        super(name);
        this.modes = modes;

        this.selectedIndex = modes.indexOf(defaultMode);

        if (this.selectedIndex < 0) {
            this.selectedIndex = 0;
        }
    }

    public List<String> getModes() {
        return modes;
    }

    public String getValue() {
        return modes.get(selectedIndex);
    }

    public void setValue(String mode) {
        int index = modes.indexOf(mode);

        if (index >= 0) {
            selectedIndex = index;
        }
    }

    public void cycle() {
        selectedIndex++;

        if (selectedIndex >= modes.size()) {
            selectedIndex = 0;
        }
    }
}