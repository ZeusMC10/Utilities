package com.zeus.utilitiesmod.setting;

public class ColorSetting extends Setting {

    private int value;

    public ColorSetting(String name, int defaultValue) {
        super(name);
        this.value = defaultValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getRed() {
        return (value >> 16) & 0xFF;
    }

    public int getGreen() {
        return (value >> 8) & 0xFF;
    }

    public int getBlue() {
        return value & 0xFF;
    }

    public void setRGB(int red, int green, int blue) {
        red = Math.clamp(red, 0, 255);
        green = Math.clamp(green, 0, 255);
        blue = Math.clamp(blue, 0, 255);

        value = 0xFF000000
                | (red << 16)
                | (green << 8)
                | blue;
    }
}