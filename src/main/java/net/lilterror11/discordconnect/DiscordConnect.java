package net.lilterror11.discordconnect;

import com.mojang.brigadier.CommandDispatcher;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.lilterror11.ConditionalValue;
import net.lilterror11.discordconnect.commands.Commands;
import net.lilterror11.discordconnect.config.ConfigManager;
import net.lilterror11.discordconnect.config.DiscordConfig;
import net.lilterror11.discordconnect.config.ModConfig;
import net.lilterror11.discordconnect.discord.Channel;
import net.lilterror11.discordconnect.discord.DiscordCommandOutput;
import net.lilterror11.discordconnect.discord.SetupDiscord;
import net.lilterror11.discordconnect.discord.commands.Command;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.Port;
import java.awt.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DiscordConnect implements ModInitializer {

    // Allowed user ids (basically discord server admins)
    public static Set<String> allowedUserIds = Set.of(
            "750301498889470002"  // Me (LilTerror11)
    );

    public static Set<String> connectedChannels;

    public static JDABuilder botBuilder;
    public static JDA bot;
    public static MinecraftServer server;
    public static CommandManager commandManager;
    public static ServerCommandSource commandSource;

    public static final Logger LOGGER = LoggerFactory.getLogger("discordconnect");

    public static ModConfig CONFIG;
    public static Channel publicChannel;
    public static Channel consoleChannel;

    public static Map<String, ServerCommandSource> discordCommandSources = new HashMap<>();;
    // Im putting it here, so it's easy to change, shut up Intellij
    private static String token;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Discord Connect");

        CONFIG = ConfigManager.loadConfig();
        token = CONFIG.botToken;
        if (token.length() != 72) {
            LOGGER.warn("A Token of the correct length has not been provided, Discord connection is disabled");
        } else {
            ServerLifecycleEvents.SERVER_STARTED.register(this::onStart);
            ServerLifecycleEvents.SERVER_STOPPED.register(this::onStop);
            ServerMessageEvents.CHAT_MESSAGE.register(EventListeners::onChatMessage);
            ServerPlayerEvents.JOIN.register(EventListeners::onPlayerJoin);
            ServerPlayerEvents.LEAVE.register(EventListeners::onPlayerLeave);
        }
        Commands.InitializeCommands();
    }

    public static ConditionalValue<Exception> loadConfig() {
        try {
            CONFIG = ConfigManager.loadConfig();
            DiscordConfig discord = CONFIG.discord;
            publicChannel = new Channel(discord.publicChannel.webhookURL, bot.getTextChannelById(discord.publicChannel.channelID));
            consoleChannel = new Channel(discord.consoleChannel.webhookURL, bot.getTextChannelById(discord.consoleChannel.channelID));
        } catch (Exception e) {
            return new ConditionalValue<>(e);
        }
        return new ConditionalValue<>();
    }


    // Runs when server starts
    public void onStart(MinecraftServer minecraftServer) {
        server = minecraftServer; // Save the server for latter use
        commandManager = server.getCommandManager(); // Command manager, for running commands :O
        commandSource = server.getCommandSource();
        server.shouldReceiveFeedback();


        // Create the bot, then initialize discord
        LOGGER.info("Connecting discord bot");
        try {
            botBuilder = JDABuilder.createLight(token,
                    EnumSet.of(GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.MESSAGE_CONTENT,
                            GatewayIntent.DIRECT_MESSAGES));
            SetupDiscord.initialize(); // Initializing
            bot = botBuilder.build();
            bot.awaitReady();

            loadConfig();
            Command.initializeCommands(bot);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Server has started!")
                    .setColor(Color.green);

            publicChannel.channel.sendMessageEmbeds(embed.build()).queue();
            consoleChannel.channel.sendMessageEmbeds(embed.build()).queue();
        } catch (Exception e) {
            LOGGER.error("Unable to connect to discord: " + e.getMessage());
        }
    }

    public void onStop(MinecraftServer minecraftServer) {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Server has stopped!!")
            .setColor(Color.red);

        publicChannel.channel.sendMessageEmbeds(embed.build()).queue();
        consoleChannel.channel.sendMessageEmbeds(embed.build()).queue();
        bot.shutdown();
    }

    public static ServerCommandSource newDiscordCommandSource(User user) {
        return new ServerCommandSource(
                new DiscordCommandOutput(),
                Vec3d.ZERO,
                Vec2f.ZERO,
                server.getOverworld(),
                3,
                "Discord@" + user.getName(),
                Text.literal("Discord@" + user.getName()),
                server,
                null
        );
    }

    public static ServerCommandSource getUserCommandSource(String userID) {
        User user = bot.getUserById(userID);

        return discordCommandSources.computeIfAbsent(
                userID,
                id -> newDiscordCommandSource(user)
        );
    }

    public static ServerCommandSource getUserCommandSource(User user) {
        String userID = user.getId();

        return discordCommandSources.computeIfAbsent(
                userID,
                id -> newDiscordCommandSource(user)
        );
    }
}
