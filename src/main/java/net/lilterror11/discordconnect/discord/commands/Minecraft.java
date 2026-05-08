package net.lilterror11.discordconnect.discord.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.lilterror11.discordconnect.DiscordConnect;

import java.util.Arrays;
import java.util.List;

public class Minecraft {
    // Class for minecraft commands
    public static List<Command.Choice> generatePlayerArgs(String input) {
        return Arrays.stream(DiscordConnect.server.getPlayerManager().getPlayerNames())
                .filter(name -> name.startsWith(input))
                .limit(25)
                .map(name -> new Command.Choice(name, name))
                .toList();
    }

    public static void KickCommand(SlashCommandInteractionEvent event) {
        String reason = "";
        if (event.getOption("reason") != null) {
            reason = " " + event.getOption("reason").getAsString();
        }
        try {
            DiscordConnect.server.getCommandManager().getDispatcher().execute("kick " + event.getOption("player").getAsString() + reason, DiscordConnect.getUserCommandSource(event.getUser()));
            event.reply("Command executed successfully.").queue();
        } catch (CommandSyntaxException e) {
            event.reply(e.getMessage()).queue();
            DiscordConnect.LOGGER.error(e.getMessage());
        }
    }
}
