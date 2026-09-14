package com.zeus.utilitiesmod.feature;

import com.zeus.utilitiesmod.setting.ColorSetting;
import com.zeus.utilitiesmod.setting.IntegerSetting;
import com.zeus.utilitiesmod.setting.ModeSetting;

import java.util.List;

public class ClockFeature extends Feature {

    private final ModeSetting format =
            new ModeSetting(
                    "Format",
                    List.of("12 Hour", "24 Hour"),
                    "12 Hour"
            );

    private final IntegerSetting x =
            new IntegerSetting("X", 5, 0, 5000);

    private final IntegerSetting y =
            new IntegerSetting("Y", 5, 0, 5000);

    private final ColorSetting color =
            new ColorSetting("Color", 0xFFFFFFFF);

    public ClockFeature() {
        super(
                "Clock",
                "Displays the real-life time on your Minecraft screen."
        );

        addSetting(format);
        addSetting(x);
        addSetting(y);
        addSetting(color);
    }

    public ModeSetting getFormat() {
        return format;
    }

    public IntegerSetting getX() {
        return x;
    }

    public IntegerSetting getY() {
        return y;
    }

    public ColorSetting getColor() {
        return color;
    }
}