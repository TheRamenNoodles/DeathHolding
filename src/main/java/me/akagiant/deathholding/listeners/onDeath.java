package me.akagiant.deathholding.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class onDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (onDamage.dyingPlayers.contains(e.getEntity())) {
            e.setDeathMessage("");
        }
    }
}
