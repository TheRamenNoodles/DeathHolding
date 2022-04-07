package me.akagiant.deathholding.listeners;

import me.akagiant.deathholding.Main;
import me.akagiant.deathholding.managers.CooldownManager;
import me.akagiant.deathholding.managers.DyingManager;
import me.akagiant.deathholding.managers.MessageManager;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class onInteract implements Listener {

    private final CooldownManager cooldownManager = new CooldownManager();

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLDEN_APPLE) {
                Player target = (Player) e.getRightClicked();
                if (DyingManager.dyingPlayers.contains(target.getUniqueId())) {
                    long timeLeft = System.currentTimeMillis() - cooldownManager.getCooldwon(e.getPlayer().getUniqueId());
                    if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.COOLDOWN) {
                        DyingManager.revivePlayer(target, e.getPlayer());
                        cooldownManager.setCooldown(e.getPlayer(), System.currentTimeMillis());
                    } else {
                        for (String str : Main.config.getConfig().getStringList("Revival.Cooldown.Message0")) {
                            e.getPlayer().sendMessage(MessageManager.internalPlaceholders(e.getPlayer(), null, str));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player && e.getDismounted().getType().equals(EntityType.ARMOR_STAND) && DyingManager.dyingPlayers.contains(Objects.requireNonNull(((Player) e.getEntity()).getPlayer()).getUniqueId())) e.setCancelled(true);
    }

}
