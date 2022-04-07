package me.akagiant.deathholding.listeners;

import me.akagiant.deathholding.DeathTimer;
import me.akagiant.deathholding.Main;

import me.akagiant.deathholding.managers.DeathManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.*;
import org.bukkit.entity.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class onDamage implements Listener {

    public static List<Player> dyingPlayers = new ArrayList<>();

    public static HashMap<Player, ArmorStand> stands = new HashMap();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        // Ensures the damage is done by a player, to a player
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player target = (Player) e.getEntity();

            if (target.getHealth() - e.getDamage() <= 0) {
                if (DeathManager.dyingPlayers.contains(target)) {
                    DeathManager.killPlayer(target);
                    return;
                }

                e.setCancelled(true);

                DeathManager.enterDying(target);
            }
        }
    }
}
