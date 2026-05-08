package net.lilterror11.discordconnect.discord;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.lilterror11.discordconnect.DiscordConnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MessageReceivedListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String content = event.getMessage().getContentDisplay();
        User author =  event.getAuthor();
        Member member = event.getMember();

        if (author.isBot()) {
            return; // Don't want to spam
        }

        boolean pass = false;

        String username;
        String globalName = author.getGlobalName() != null ? author.getGlobalName() : author.getName();
        if (event.isFromType(ChannelType.PRIVATE)) {
            if (!DiscordConnect.allowedUserIds.contains(author.getId())) {
                author.openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage("You do not have permission to use a DM").queue();
                });
            }
            pass = true;
            username = author.getEffectiveName();
        } else {
            if (member == null) {
                username = author.getEffectiveName();
            } else {
                username = member.getEffectiveName();
            }
        }

        Text discordText;

        if (event.getChannel().getId().equals(DiscordConnect.publicChannel.channel.getId()) || pass) {
            discordText = Text.literal("ᴅɪѕᴄᴏʀᴅ").styled(style -> style.withBold(true).withColor(Formatting.BLUE).withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Hello from ")
                                    .append(Text.literal("Discord").styled(style1 -> style1.withColor(Formatting.BLUE)))
                                    .append(Text.literal("!"))
                    )
            ));
        } else if (event.getChannel().getId().equals(DiscordConnect.consoleChannel.channel.getId())) {
            discordText = Text.literal("ᴄᴏɴѕᴏʟᴇ").styled(style -> style.withBold(true).withColor(Formatting.RED).withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Hello from ")
                                    .append(Text.literal("Discord").styled(style1 -> style1.withColor(Formatting.BLUE)))
                                    .append(Text.literal("!\nThis has been sent from the "))
                                    .append(Text.literal("ᴄᴏɴѕᴏʟᴇ").styled(style1 -> style1.withColor(Formatting.RED).withBold(true)))
                                    .append(Text.literal(" Channel"))
                    )
            ));
        } else {
            DiscordConnect.LOGGER.info("It wasn't this fucking channel APARENTLY, here you fucking idiot, check the name because aparently im shit at this " + event.getChannel().getName()); // Don't look
            return;
        }

        Text message = Text.empty()
                .append(discordText)
                .append(Text.literal(" <"))
                .append(Text.literal(username).styled(style -> style.withHoverEvent(
                        new HoverEvent.ShowText(
                                Text.literal(globalName).styled(style1 -> style1.withColor(Formatting.YELLOW))
                                        .append(Text.literal("\n@" + author.getName()).styled(style1 -> style1.withColor(Formatting.GRAY)))
                        )
                ).withColor(Formatting.YELLOW)))
                .append(Text.literal("> "))
                .append(Text.literal(content));

        MinecraftServer server = DiscordConnect.server;
        server.getPlayerManager().broadcast(message, false);
    }
}
