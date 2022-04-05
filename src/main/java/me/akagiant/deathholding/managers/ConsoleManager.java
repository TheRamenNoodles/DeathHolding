package me.akagiant.deathholding.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ConsoleManager {

    public static void InfoToConsole(@NotNull String prefix, @NotNull String message) {
        prefix = "&8[&e"+ prefix + " INFO&8]";
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&',prefix + " " + message));
    }
    public static void WarnToConsole(@NotNull String prefix, @NotNull String message) {
        prefix = "&8[&e"+ prefix + " WARNING&8]";
        Bukkit.getLogger().warning(ChatColor.translateAlternateColorCodes('&', prefix + " " + message));
    }

    public static void ErrorToConsole(@NotNull String prefix, @NotNull String message) {
        prefix = "&8[&c"+ prefix + " ERROR&8]";
        Bukkit.getLogger().severe(ChatColor.translateAlternateColorCodes('&', prefix + " " + message));
    }

}
