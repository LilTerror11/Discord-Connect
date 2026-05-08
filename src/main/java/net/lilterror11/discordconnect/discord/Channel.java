package net.lilterror11.discordconnect.discord;

import net.dv8tion.jda.api.entities.IncomingWebhookClient;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.lilterror11.discordconnect.DiscordConnect;

public class Channel {
    public IncomingWebhookClient webhook;
    public TextChannel channel;
    public Channel(String webhookUrl, TextChannel channel1) {
        channel = channel1;
        webhook = WebhookClient.createClient(DiscordConnect.bot, webhookUrl);
    }
}
