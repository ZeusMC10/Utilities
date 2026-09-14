package com.zeus.utilitiesmod.setting;

public class IntegerSetting extends Setting {

    private int value;

    private final int min;
    private final int max;

    public IntegerSetting(
            String name,
            int defaultValue,
            int min,
            int max
    ) {
        super(name);
        this.value = defaultValue;
        this.min = min;
        this.max = max;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = Math.clamp(value, min, max);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void setValueFromString(String value) {
        try {
            setValue(Integer.parseInt(value));
        } catch (NumberFormatException ignored) {

        }
    }
}