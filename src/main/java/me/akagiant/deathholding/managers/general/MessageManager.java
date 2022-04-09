package me.akagiant.deathholding.managers.general;

import me.akagiant.deathholding.managers.CooldownManager;
import me.akagiant.deathholding.managers.DyingManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MessageManager {

    static CooldownManager cooldownManager = new CooldownManager();

    public static String internalPlaceholders(@Nullable Player player, @Nullable Player target, @NotNull String msg) {
        if (player != null) {
            msg = msg.replace("%player_name%", player.getName());
            msg = msg.replace("%player_cooldown%", String.valueOf(cooldownManager.getCooldwon(target.getUniqueId()) - System.currentTimeMillis()));
            msg = msg.replace("%player_isDying%", String.valueOf(DyingManager.isDying(player)));
        }

        if (target != null) {
            msg = msg.replace("%target_name%", target.getName());
            msg = msg.replace("%target_cooldown%", String.valueOf(cooldownManager.getCooldwon(target.getUniqueId()) - System.currentTimeMillis()));
            msg = msg.replace("%target_isDying%", String.valueOf(DyingManager.isDying(target)));
        }
        return ColorManager.format(msg);
    }

}
