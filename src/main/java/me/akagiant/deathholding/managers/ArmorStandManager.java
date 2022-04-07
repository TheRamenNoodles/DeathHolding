package me.akagiant.deathholding.managers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public class ArmorStandManager {

    private static final HashMap<ArmorStand, UUID> stands = new HashMap<>();

    static ArmorStand createStand(Player player, Location loc, String name) {
        World world = player.getWorld();

        ArmorStand am = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);

        if (name != null) {
            am.setCustomName(name);
            am.setCustomNameVisible(true);
        }

        am.setGravity(false);
        am.setInvisible(false);
        am.setSmall(true);
        am.setMarker(true);
        am.setInvulnerable(false);

        stands.put(am, player.getUniqueId());

        return am;
    }

    static ArmorStand createStand(Player player) {
        Location loc = player.getLocation();
        return createStand(player, loc, null);
    }

    static ArmorStand createStand(Player player, ArmorStand am) {
        return createStand(player, am);
    }

    static List<ArmorStand> getStands(Player player) {
        List<ArmorStand> playerStands = new ArrayList<>();

        for (Map.Entry<ArmorStand, UUID> entry : stands.entrySet()) {
            if (entry.getValue().equals(player.getUniqueId())) {
                playerStands.add(entry.getKey());
            }

        }
        return playerStands;
    }

    public static void removeStands(Player player) {
        List<ArmorStand> playerStands = getStands(player);
        for (ArmorStand stand : playerStands) {
            stand.remove();
        }
    }

}
