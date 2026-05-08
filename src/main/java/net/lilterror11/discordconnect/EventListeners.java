package net.lilterror11.discordconnect;

import net.dv8tion.jda.api.EmbedBuilder;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.awt.*;

public class EventListeners {
    public static void onChatMessage(
            SignedMessage message,
            ServerPlayerEntity player,
            MessageType.Parameters parameters
    ) {
        String username = player.getName().getString();
        DiscordConnect.LOGGER.info("Got message from " + username + ": " + message.getContent().getString());
        DiscordConnect.publicChannel.webhook.sendMessage(message.getContent().getString()).setAvatarUrl("https://mc-heads.net/avatar/" + username).setUsername(username).queue();
        DiscordConnect.consoleChannel.webhook.sendMessage(message.getContent().getString()).setAvatarUrl("https://mc-heads.net/avatar/" + username).setUsername(username).queue();
    }

    public static void onPlayerJoin(
            ServerPlayerEntity player
    ) {
        String username = player.getName().getString();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("**" + username + "** Joined the game.")
                .setThumbnail("https://minotar.net/avatar/" + username + "/25")
                .setColor(Color.green);
        DiscordConnect.publicChannel.channel.sendMessageEmbeds(embed.build()).queue();
        DiscordConnect.consoleChannel.channel.sendMessageEmbeds(embed.setDescription("UUID: " + player.getUuid().toString()).build()).queue();
    }
    public static void onPlayerLeave(
            ServerPlayerEntity player
    ) {
        String username = player.getName().getString();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("**" + username + "** Left the game.")
                .setThumbnail("https://minotar.net/avatar/" + username + "/20")
                .setColor(Color.red);
        DiscordConnect.publicChannel.channel.sendMessageEmbeds(embed.build()).queue();
        DiscordConnect.consoleChannel.channel.sendMessageEmbeds(embed.setDescription("UUID: " + player.getUuid().toString()).build()).queue();
    }
}
