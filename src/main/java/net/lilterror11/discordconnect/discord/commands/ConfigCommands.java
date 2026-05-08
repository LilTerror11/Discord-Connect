package net.lilterror11.discordconnect.discord.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.lilterror11.discordconnect.DiscordConnect;

public class ConfigCommands {
    public static void ConfigCommandRoot(SlashCommandInteractionEvent event) {
        switch (event.getSubcommandName()) {
            case "reload_config" -> ConfigCommands.reloadConfig(event);
            case null, default -> {}
        }
    }

    public static void reloadConfig(SlashCommandInteractionEvent event) {
        try {
            DiscordConnect.server.getCommandManager().getDispatcher().execute("config reloadConfig", DiscordConnect.getUserCommandSource(event.getUser()));
            event.reply("Command executed successfully.").queue();
        } catch (CommandSyntaxException e) {
            DiscordConnect.LOGGER.error(e.getMessage());
            event.reply(e.getMessage());
        }
    }
}
