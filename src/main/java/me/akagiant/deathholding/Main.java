package me.akagiant.deathholding;

import me.akagiant.deathholding.commands.command_clear;
import me.akagiant.deathholding.commands.command_reset;
import me.akagiant.deathholding.files.ConfigManager;
import me.akagiant.deathholding.listeners.onDamage;
import me.akagiant.deathholding.listeners.onDeath;
import me.akagiant.deathholding.listeners.onDismount;
import me.akagiant.deathholding.listeners.onInteract;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    static Main plugin;
    public static ConfigManager config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        config = new ConfigManager(this, "config");

        getServer().getPluginManager().registerEvents(new onDamage(), this);
        getServer().getPluginManager().registerEvents(new onDismount(), this);
        getServer().getPluginManager().registerEvents(new onDeath(), this);
        getServer().getPluginManager().registerEvents(new onInteract(), this);

        getCommand("reset").setExecutor(new command_reset(this));
        getCommand("clear").setExecutor(new command_clear(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getPlugin() {
        return plugin;
    }
}
