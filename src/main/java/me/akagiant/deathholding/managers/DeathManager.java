package me.akagiant.deathholding.managers;

import me.akagiant.deathholding.DeathTimer;
import me.akagiant.deathholding.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeathManager {

    public static final List<Player> dyingPlayers = new ArrayList<>();

    /**
     * Provided player will enter the "dying" state.
     * @param player | Who will enter the dying state
     */
    public static void enterDying(Player player) {
        dyingPlayers.add(player);

        deathTimer(player);

        // START | Add effects to player
        player.setGlowing(true);
        player.setHealth(0.5);
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1300, 10));
        player.sendMessage("You should have died but I saved you... for now.");
        // END | Adding effects to player

        ArmorStandManager.createStand(player, player.getLocation().add(0, 2, 0), ChatColor.translateAlternateColorCodes('&', "&c&lDYING"));

        // Create new armor stand and force the player to be a passenger of the armor stand.
        ArmorStand am2 = ArmorStandManager.createStand(player);
        am2.addPassenger(player);
    }

    public static void revivePlayer(Player target, Player reviver) {
        clearEffects(target);
        ArmorStandManager.removeStands(target);
        target.setHealth(6);
        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5, 2));
        dyingPlayers.remove(target);
        target.sendMessage("You have been revived by " + reviver.getName());
        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    public static void killPlayer(Player target) {
        clearEffects(target);
        ArmorStandManager.removeStands(target);

        target.setHealth(0);
        dyingPlayers.remove(target);
    }

    private static void clearEffects(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.setGlowing(false);
    }



    static void deathTimer(Player player) {
        new DeathTimer(60, Main.getPlugin()) {
            @Override
            public void count(int current) {
                if (!dyingPlayers.contains(player)) {
                    ArmorStandManager.removeStands(player);
                    task.cancel();
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Time Remaining: " + current + " seconds!"));
                if (current == 0) {
                    killPlayer(player);
                }

            }
        }.start();
    }

}
