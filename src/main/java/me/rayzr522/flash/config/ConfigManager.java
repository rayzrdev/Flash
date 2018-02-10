package me.rayzr522.flash.config;

import me.rayzr522.flash.FlashPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigManager {
    private final FlashPlugin plugin;

    public ConfigManager(FlashPlugin plugin) {
        this.plugin = plugin;
    }

    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    public File getFile(String path) {
        return new File(getDataFolder(), path.replace('/', File.separatorChar));
    }

    public YamlConfiguration getConfig(String path) {
        File file = getFile(path);
        if (!file.exists() && plugin.getResource(path) != null) {
            plugin.saveResource(path, false);
        }
        return YamlConfiguration.loadConfiguration(getFile(path));
    }

    public boolean saveConfig(String path, YamlConfiguration configuration) {
        File file = getFile(path);

        try {
            configuration.save(file);
            return true;
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save config at '{}'", path);
            return false;
        }
    }
}
