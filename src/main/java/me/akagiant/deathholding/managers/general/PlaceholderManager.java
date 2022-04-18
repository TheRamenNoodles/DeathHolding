package me.akagiant.deathholding.managers.general;

import me.akagiant.deathholding.managers.CooldownManager;
import me.akagiant.deathholding.managers.DyingManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderManager extends PlaceholderExpansion {

    CooldownManager cooldownManager = new CooldownManager();

    @Override
    public @NotNull String getIdentifier() {
        return "DeathHolding";
    }

    @Override
    public @NotNull String getAuthor() {
        return "AkaGiant";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {

        switch(params.toLowerCase()) {
            // Eco Placeholders
            case "cooldown": return String.valueOf(cooldownManager.getCooldwon(offlinePlayer.getUniqueId()) - System.currentTimeMillis());
            case "isDying": return String.valueOf(DyingManager.isDying((Player) offlinePlayer));
            default: return null;
        }
    }
}
