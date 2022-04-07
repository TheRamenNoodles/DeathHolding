package me.akagiant.deathholding.listeners;

import me.akagiant.deathholding.DeathTimer;
import me.akagiant.deathholding.Main;

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
                if (dyingPlayers.contains(target)) {
                    target.setHealth(0);
                    target.setGlowing(false);
                    dyingPlayers.remove(target);
                    return;
                }

                e.setCancelled(true);
                dyingPlayers.add(target);

                target.setGlowing(true);
                target.setHealth(0.5);
                target.playSound(target.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1300, 10));
                target.sendMessage("You should have died but I saved you... for now.");

                ArmorStand am = getTagStand(target.getWorld(), target.getLocation().add(0, 1.5, 0), "lol");
                ArmorStand am2 = getTagStand(target.getWorld(), target.getLocation(), null);

                am2.addPassenger(target);

                new DeathTimer(60, Main.getPlugin()) {
                    @Override
                    public void count(int current) {
                        if (!dyingPlayers.contains(target)) {
                            am.remove();
                            am2.remove();
                            task.cancel();
                        }
                        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Time Remaining: " + current + " seconds!"));
                        if (current == 0) {
                            am.remove();
                            am2.remove();
                            target.setHealth(0);
                            target.setGlowing(false);
                        }

                    }
                }.start();
            }
        }
    }


    static ArmorStand getTagStand(World world, Location loc, String name) {
        ArmorStand am = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);

        if (name != null) {
            am.setCustomName(name);
            am.setCustomNameVisible(true);
        }

        am.setGravity(false);
        am.setInvisible(true);
        am.setSmall(true);
        am.setMarker(true);
        am.setInvulnerable(false);

        return am;
    }

}
