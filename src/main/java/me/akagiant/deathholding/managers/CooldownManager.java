package me.akagiant.deathholding.managers;

import me.akagiant.deathholding.Main;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    public static final Long COOLDOWN = Main.config.getConfig().getLong("Revival.Cooldown.Time");

    public void setCooldown (Player player, Long time) {
        if (player.hasPermission("DeathHolding.Cooldown.Bypass")) return;

        if (time < 1) cooldowns.remove(player.getUniqueId());
        else cooldowns.put(player.getUniqueId(), time);
    }

    public Long getCooldwon(UUID player) {
        return cooldowns.getOrDefault(player, COOLDOWN);
    }


}
