package me.akagiant.deathholding;

import me.akagiant.deathholding.commands.command_deathholding;
import me.akagiant.deathholding.managers.files.ConfigManager;
import me.akagiant.deathholding.listeners.onDamage;
import me.akagiant.deathholding.listeners.onInteract;
import me.akagiant.deathholding.managers.general.PlaceholderManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    static Main plugin;
    public static ConfigManager config;

    // TODO: Add PlaceHolderAPI

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        config = new ConfigManager(this, "config");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderManager().register();
        } else {
            Bukkit.getLogger().info("PlaceholderAPI Not Found. You will not be able to use my placeholders externally.");
        }

        getCommand("dh").setExecutor(new command_deathholding(this));
        getCommand("dh").setTabCompleter(new command_deathholding(this));

        getServer().getPluginManager().registerEvents(new onDamage(), this);
        getServer().getPluginManager().registerEvents(new onInteract(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getPlugin() {
        return plugin;
    }
}
