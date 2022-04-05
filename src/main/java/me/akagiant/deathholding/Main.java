package me.akagiant.deathholding;

import me.akagiant.deathholding.files.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    static Main plugin;
    public static ConfigManager config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        config = new ConfigManager(this, "config");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getPlugin() {
        return plugin;
    }
}
