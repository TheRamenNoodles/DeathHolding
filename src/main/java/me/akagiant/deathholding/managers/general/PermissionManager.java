package me.akagiant.deathholding.managers.general;

import me.akagiant.deathholding.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PermissionManager {

    public static void NoPermission(Player player, String permission) {
        for (String msg : Main.config.getConfig().getStringList("NoPermission.ConsoleMessage.Message")) {
            msg = msg.replace("%PlayerName%", player.getName());
            msg = msg.replace("%MissingPermission%", permission);
            Bukkit.getLogger().info(ColorManager.format(msg));
        }

        for (String msg : Main.config.getConfig().getStringList("NoPermission.Player.Message")) {
            msg = msg.replace("%PlayerName%", player.getName());
            msg = msg.replace("%MissingPermission%", permission);
            player.sendMessage(ColorManager.format(msg));
        }
    }

}

