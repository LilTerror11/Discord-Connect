package net.lilterror11.discordconnect.discord;

import net.lilterror11.discordconnect.DiscordConnect;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

public class DiscordCommandOutput implements CommandOutput {
    @Override
    public void sendMessage(Text message) {
        DiscordConnect.consoleChannel.channel.sendMessage("```\n" + message.getString() + "\n```").queue();
    }

    @Override
    public boolean shouldReceiveFeedback() {
        return true;
    }

    @Override
    public boolean shouldTrackOutput() {
        return true;
    }

    @Override
    public boolean shouldBroadcastConsoleToOps() {
        return true;
    }
}
