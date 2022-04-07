package me.akagiant.deathholding.managers;

import me.akagiant.deathholding.Main;
import org.bukkit.entity.Player;

public class PermissionManager {

    public static void NoPermission(Player player, String prefix, String permission) {
        if (Main.config.getConfig().getBoolean("Messages.NoPermission.ConsoleMessage.Enable")) {
            for (String msg : Main.config.getConfig().getStringList("Messages.NoPermission.ConsoleMessage.Message")) {
                msg = msg.replace("%PlayerName%", player.getName());
                msg = msg.replace("%MissingPermission%", permission);
                ConsoleManager.WarnToConsole(prefix, msg);
            }
        }

        for (String msg : Main.config.getConfig().getStringList("Messages.NoPermission.PlayerMessage")) {
            msg = msg.replace("%PlayerName%", player.getName());
            msg = msg.replace("%MissingPermission%", permission);
            player.sendMessage(ColorManager.format(msg));
        }
    }

}
