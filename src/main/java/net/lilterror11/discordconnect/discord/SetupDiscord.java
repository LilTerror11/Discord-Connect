package net.lilterror11.discordconnect.discord;

import net.dv8tion.jda.api.JDABuilder;
import net.lilterror11.discordconnect.DiscordConnect;
import net.lilterror11.discordconnect.discord.commands.Command;

public class SetupDiscord {
    public static void initialize() {

        // easy of access
        final JDABuilder botBuilder = DiscordConnect.botBuilder;
        // Initialize the discord bot, like setting up commands and stuff

        DiscordConnect.LOGGER.info("");
        botBuilder.addEventListeners(new MessageReceivedListener());
        botBuilder.addEventListeners(new Command());
    }
}
