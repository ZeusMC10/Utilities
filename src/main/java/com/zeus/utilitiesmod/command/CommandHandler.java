package com.zeus.utilitiesmod.command;

import com.zeus.utilitiesmod.gui.UtilitiesScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;

public class CommandHandler {

    public static void initialize() {

        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, buildContext) -> {

                    dispatcher.register(
                            ClientCommands.literal("utilities")
                                    .executes(context -> {
                                        Minecraft minecraft = Minecraft.getInstance();

                                        minecraft.execute(() ->
                                                minecraft.setScreen(
                                                        new UtilitiesScreen()
                                                )
                                        );

                                        return 1;
                                    })
                    );

                    dispatcher.register(
                            ClientCommands.literal("utils")
                                    .executes(context -> {
                                        Minecraft minecraft = Minecraft.getInstance();

                                        minecraft.execute(() ->
                                                minecraft.setScreen(
                                                        new UtilitiesScreen()
                                                )
                                        );

                                        return 1;
                                    })
                    );
                }
        );
    }
}