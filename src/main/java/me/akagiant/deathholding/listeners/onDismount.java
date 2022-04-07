package me.akagiant.deathholding.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class onDismount implements Listener {

    /**
     * Stop player from dismounting the armor stand when they are "dying"
     */

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player && e.getDismounted().getType().equals(EntityType.ARMOR_STAND)) {
            e.getEntity().sendMessage("hi");
        }
    }

}
