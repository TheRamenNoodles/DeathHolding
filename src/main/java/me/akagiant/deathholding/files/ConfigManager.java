package me.akagiant.deathholding.files;

import me.akagiant.deathholding.managers.ConsoleManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigManager {

    Plugin plugin;
    String fileName;
    private FileConfiguration config;
    private File file;

    public ConfigManager(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        file = new File(Bukkit.getServer().getPluginManager().getPlugin(plugin.getName()).getDataFolder(), File.separator + fileName + ".yml");
        config = YamlConfiguration.loadConfiguration(file);
        saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }

    public boolean exists() {
        return file.exists();
    }

    public void saveConfig() {
        try {
            this.getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig() {

        if (file == null)
            file = new File(Bukkit.getServer().getPluginManager().getPlugin(plugin.getName()).getDataFolder(), File.separator + fileName + ".yml");

        if (!file.exists()) {
            plugin.saveResource(fileName + ".yml", false);
        }
    }

    public void reloadConfig() {
        if (!exists()) {
            ConsoleManager.ErrorToConsole(plugin.getName(), file.getName() + ".yml does not exist!");
            return;
        }
        config = YamlConfiguration.loadConfiguration(file);
        InputStream stream = plugin.getResource(fileName);
        if (stream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
            config.setDefaults(defaultConfig);
        }
    }
}

