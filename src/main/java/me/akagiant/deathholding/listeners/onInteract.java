package me.akagiant.deathholding.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class onInteract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLDEN_APPLE) {
                Player target = (Player) e.getRightClicked();
                if (onDamage.dyingPlayers.contains(target)) {
                    onDamage.dyingPlayers.remove(target);
                    target.setGlowing(false);
                    target.sendMessage("You have been revived by " + e.getPlayer());
                }
            }
        }
    }

}
