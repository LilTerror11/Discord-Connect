package net.lilterror11.discordconnect.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.lilterror11.discordconnect.DiscordConnect;

import java.util.List;

public class Command extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        DiscordConnect.LOGGER.info(event.getName());
        switch (event.getName()) {
            case "config" -> ConfigCommands.ConfigCommandRoot(event);
            case "kick" -> Minecraft.KickCommand(event);
        }
    }

    public static void initializeCommands(JDA bot) {
        List<CommandData> commands = List.of(
                Commands.slash("config", "Various config based commands")
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .addSubcommands(
                        new SubcommandData("reload_config", "Reloads the config from file")
                    ),
                Commands.slash("kick", "/kick <targets> [<reason>]")
                    .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .addOption(OptionType.STRING, "player", "<targets>", true, true)
                    .addOption(OptionType.STRING, "reason", "[<reason>]")
        );
        if (DiscordConnect.CONFIG.developmentMode) {
            bot.getGuildById(DiscordConnect.CONFIG.discord.developmentServerID).updateCommands().addCommands(commands).queue();
        } else {
            bot.updateCommands().addCommands(commands).queue();
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (event.getName().equals("kick") && event.getFocusedOption().getName().equals("player")) {
            String input = event.getFocusedOption().getValue();
            event.replyChoices(Minecraft.generatePlayerArgs(input)).queue();
        }
    }
}

