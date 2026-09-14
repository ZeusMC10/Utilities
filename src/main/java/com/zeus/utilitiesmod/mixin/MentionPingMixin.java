package com.zeus.utilitiesmod.mixin;

import com.zeus.utilitiesmod.UtilitiesClient;
import com.zeus.utilitiesmod.feature.MentionPingFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import com.mojang.authlib.GameProfile;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatListener.class)
public class MentionPingMixin {

    @Inject(
            method = "handlePlayerChatMessage",
            at = @At("HEAD")
    )
    private void utilities$checkMention(
            PlayerChatMessage message,
            GameProfile sender,
            ChatType.Bound boundChatType,
            CallbackInfo ci
    ) {
        MentionPingFeature mentionPing = (MentionPingFeature) UtilitiesClient.FEATURE_MANAGER.getFeature("Mention Ping");

        if (mentionPing == null || !mentionPing.isEnabled()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) {
            return;
        }

        if (message.sender().equals(player.getUUID())) {
            return;
        }

        String username = player.getGameProfile().name();
        String text = message.decoratedContent().getString();

        String usernameToMatch = username;
        String textToMatch = text;

        if (!mentionPing.getCaseSensitive().getValue()) {
            usernameToMatch = usernameToMatch.toLowerCase();
            textToMatch = textToMatch.toLowerCase();
        }

        if (textToMatch.matches(
                ".*\\b" +
                        java.util.regex.Pattern.quote(usernameToMatch) +
                        "\\b.*"
        )) {
            player.playSound(
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    1.0F,
                    1.0F
            );
        }
    }

    @Inject(
            method = "handleSystemMessage",
            at = @At("HEAD")
    )
    private void utilities$checkSystemMessage(
            Component message,
            boolean remote,
            CallbackInfo ci
    ) {
        MentionPingFeature mentionPing =
                (MentionPingFeature) UtilitiesClient.FEATURE_MANAGER
                        .getFeature("Mention Ping");

        if (mentionPing == null || !mentionPing.isEnabled()) {
            return;
        }

        if (!mentionPing.getSystemMessages().getValue()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) {
            return;
        }

        String username = player.getGameProfile().name();
        String text = message.getString();

        String usernameToMatch = username;
        String textToMatch = text;

        if (!mentionPing.getCaseSensitive().getValue()) {
            usernameToMatch = usernameToMatch.toLowerCase();
            textToMatch = textToMatch.toLowerCase();
        }

        if (textToMatch.matches(
                ".*\\b" +
                        java.util.regex.Pattern.quote(usernameToMatch) +
                        "\\b.*"
        )) {
            player.playSound(
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    1.0F,
                    1.0F
            );
        }
    }
}