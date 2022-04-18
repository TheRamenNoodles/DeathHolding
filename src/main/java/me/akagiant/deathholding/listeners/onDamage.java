package me.akagiant.deathholding.listeners;

import me.akagiant.deathholding.managers.DyingManager;

import org.bukkit.entity.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class onDamage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player target = (Player) e.getEntity();

            if (target.getHealth() - e.getDamage() <= 0) {
                if (DyingManager.dyingPlayers.contains(target.getUniqueId())) {
                    DyingManager.killPlayer(target, (Player) e.getDamager());
                    return;
                }

                e.setCancelled(true);
                DyingManager.enterDying(target, ((Player) e.getDamager()).getPlayer());
            }
        }

        if (e.getDamager() instanceof Mob && e.getEntity() instanceof Player) {
            if (DyingManager.dyingPlayers.contains(e.getEntity().getUniqueId()) && ((Player) e.getEntity()).getHealth() - e.getDamage() <= 0) {
                e.setCancelled(true);
                DyingManager.killPlayer((Player) e.getEntity(), (Mob) e.getDamager());
            }
        }
    }
}
