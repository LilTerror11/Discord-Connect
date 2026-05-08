package net.lilterror11.discordconnect.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.lilterror11.discordconnect.DiscordConnect;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    public static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("discordConnect.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ModConfig loadConfig() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                return GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                DiscordConnect.LOGGER.warn("Failed to read config " + e.getMessage());
            }
        }
        ModConfig defaultConfig = new ModConfig();
        saveConfig(defaultConfig);
        return defaultConfig;
    }

    public static void saveConfig(ModConfig config) {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            DiscordConnect.LOGGER.warn("Failed to save config " + e.getMessage());
        }
    }
}
