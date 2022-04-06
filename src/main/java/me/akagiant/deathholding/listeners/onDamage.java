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

            Location loc = target.getLocation();
            loc.setY(loc.getY() + 2);

            if (target.getHealth() - e.getDamage() <= 0) {

                ArmorStand am = getTagStand(target.getWorld(), target.getLocation().add(0, -2, 0), "lol");
                ArmorStand am2 = getTagStand(target.getWorld(), target.getLocation(), null);

                stands.put(target, am);
                stands.put(target, am2);

                if (dyingPlayers.contains(target)) {
                    target.setHealth(0);
                    am2.remove();
                    am.remove();
                    target.setGlowing(false);
                    dyingPlayers.remove(target);
                    return;
                }


                dyingPlayers.add(target);

                e.setCancelled(true);
                target.setHealth(0.5);
                target.playSound(target.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1300, 10));
                target.sendMessage("You should have died but I saved you... for now.");
                am.addPassenger(target);

                target.setGlowing(true);

                new DeathTimer(60, Main.getPlugin()) {
                    @Override
                    public void count(int current) {
                        if (!dyingPlayers.contains(target)) {
                            task.cancel();
                        }
                        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Time Remaining: " + current + " seconds!"));
                    }
                }.start();

                BukkitScheduler scheduler = Bukkit.getScheduler();
                scheduler.scheduleSyncDelayedTask(Main.getPlugin(), () -> {
                    am.remove();
                    am2.remove();
                    target.setHealth(0);
                    target.setGlowing(false);
                }, 1200);

            }
        }
    }


    static ArmorStand getTagStand(World world, Location loc, String name) {
        ArmorStand am = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);

        if (name != null) {
            am.setCustomName("ashnfklanmf");
            am.setCustomNameVisible(true);
        }

        am.setGravity(false);
        am.setInvisible(true);
        am.setSmall(true);
        am.setMarker(true);
        am.setInvulnerable(true);

        return am;
    }

}
