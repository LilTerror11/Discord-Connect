package net.lilterror11.discordconnect.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.lilterror11.ConditionalValue;
import net.lilterror11.discordconnect.DiscordConnect;
import net.lilterror11.discordconnect.Format;
import net.lilterror11.discordconnect.config.ConfigManager;
import net.lilterror11.discordconnect.config.ModConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ConfigCommands {
    public static void initializeCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("config").requires(source -> source.hasPermissionLevel(2))
                            .executes(context -> {
                                context.getSource().sendFeedback(() -> Text.literal("""
                                        Subcommands:
                                          /config reloadConfig
                                        """), false);
                                return 1;
                            })
                    .then(literal("reloadConfig").executes(ConfigCommands::reloadConfig))
                    .then(literal("set")
                            .then(literal("minecraft")
                                .then(literal("minecraftSpecialFormating")
                                        .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            boolean value = BoolArgumentType.getBool(context, "value");
                                            ModConfig config = ConfigManager.loadConfig();
                                            config.minecraft.minecraftSpecialFormating = value;
                                            ConfigManager.saveConfig(config);
                                            return 1;
                                        }))
                                )
                            )
                    )
                    .then(literal("get")
                            .then(literal("minecraft")
                                .then(literal("minecraftSpecialFormating")
                                        .executes(context -> {
                                            boolean value = BoolArgumentType.getBool(context, "value");
                                            ModConfig config = ConfigManager.loadConfig();
                                            config.minecraft.minecraftSpecialFormating = value;
                                            ConfigManager.saveConfig(config);
                                            return 1;
                                        })
                                )
                            )
                    )
            );
        });
    }

    public static int reloadConfig(CommandContext<ServerCommandSource> context) {
        
        ServerCommandSource source = context.getSource();
        String message = "```ansi\n" + Format.INFO + " " + source.getName() + " has Attempted to reload config.```";
        if (!source.isExecutedByPlayer()) {
            message = "```ansi\n" + Format.INFO + " " + source.getName() + " has Attempted to reload config.```";
        }
        DiscordConnect.consoleChannel.channel.sendMessage(message).queue();

        /*source.sendFeedback(() -> Text.literal("Reloading ").styled(style -> style.withColor(Formatting.DARK_RED))
                .append(Text.literal("Discord").styled(style -> style.withColor(Formatting.BLUE)))
                .append(Text.literal("Connect").styled(style -> style.withColor(Formatting.GREEN)))
                .append(Text.literal(" config")), false);*/
        source.sendFeedback(() -> Text.literal("Reloading DiscordConnect config"), true);
        ConditionalValue<Exception> configReloaded =  DiscordConnect.loadConfig();
        if (configReloaded.has) {
            source.sendFeedback(() -> Text.literal("Unable to reload config...").styled(style -> style.withColor(Formatting.DARK_RED)), false);
            DiscordConnect.consoleChannel.channel.sendMessage("```ansi\n" + Format.ERROR + " Config has failed to reload.```").queue();
            return 0;
        }
        source.sendFeedback(() -> Text.literal("Config Reloaded!").styled(style -> style.withColor(Formatting.GREEN)), false);
        DiscordConnect.consoleChannel.channel.sendMessage("```ansi\n" + Format.INFO + " Config has reloaded.```").queue();
        return 1;
    }
}
