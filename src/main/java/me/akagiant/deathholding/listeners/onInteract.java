package me.akagiant.deathholding.listeners;

import me.akagiant.deathholding.managers.CooldownManager;
import me.akagiant.deathholding.managers.DeathManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.TimeUnit;

public class onInteract implements Listener {

    private final CooldownManager cooldownManager = new CooldownManager();

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLDEN_APPLE) {
                Player target = (Player) e.getRightClicked();
                if (DeathManager.dyingPlayers.contains(target.getUniqueId())) {
                    long timeLeft = System.currentTimeMillis() - cooldownManager.getCooldwon(e.getPlayer().getUniqueId());
                    if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.COOLDOWN) {
                        DeathManager.revivePlayer(target, e.getPlayer());
                        cooldownManager.setCooldown(e.getPlayer(), System.currentTimeMillis());
                    } else {
                        e.getPlayer().sendMessage(ChatColor.RED.toString() + (CooldownManager.COOLDOWN - TimeUnit.MILLISECONDS.toSeconds(timeLeft)) + " seconds before you can use this feature again.");
                    }
                }
            }
        }
    }

}
