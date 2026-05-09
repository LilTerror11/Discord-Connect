package net.lilterror11.discordconnect.config;

import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    public String botToken = "";
    public List<String> privilegedUsers = new ArrayList<>();
    public DiscordConfig discord = new DiscordConfig();
    public MinecraftConfig minecraft = new MinecraftConfig();
    public boolean developmentMode = false;
}
