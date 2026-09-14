package com.zeus.utilitiesmod.feature;

import com.zeus.utilitiesmod.setting.BooleanSetting;

public class MentionPingFeature extends Feature {

    private final BooleanSetting caseSensitive =
            new BooleanSetting("Case Sensitive", false);

    private final BooleanSetting systemMessages =
            new BooleanSetting("System Messages", false);

    public MentionPingFeature() {
        super("Mention Ping", "Plays a sound when your in-game name is mentioned in chat.");

        addSetting(caseSensitive);
        addSetting(systemMessages);
    }

    public BooleanSetting getCaseSensitive() {
        return caseSensitive;
    }

    public BooleanSetting getSystemMessages() {
        return systemMessages;
    }
}