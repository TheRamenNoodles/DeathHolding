package me.akagiant.deathholding.listeners;

import me.akagiant.deathholding.managers.DeathManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class onDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (DeathManager.dyingPlayers.contains(e.getEntity())) {
            e.setDeathMessage("");
        }
    }
}
