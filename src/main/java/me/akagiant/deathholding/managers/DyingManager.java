package me.akagiant.deathholding.managers;

import me.akagiant.deathholding.Main;
import me.akagiant.deathholding.managers.general.ColorManager;
import me.akagiant.deathholding.managers.general.MessageManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DyingManager {

    public static final List<UUID> dyingPlayers = new ArrayList<>();
    static FileConfiguration config = Main.config.getConfig();


    public static boolean isDying(Player player) {
        return dyingPlayers.contains(player.getUniqueId());
    }

    /**
     * Provided player will enter the "dying" state.
     * @param player | Who will enter the dying state
     */
    public static void enterDying(Player player, Player killer) {

        if (player.hasPermission("DeathHolding.Bypass")) return;

        dyingPlayers.add(player.getUniqueId());

        deathTimer(player);

        // START | Add effects to player
        player.setGlowing(true);
        player.setHealth(0.5);
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1300, 10));

        for (String str : Main.config.getConfig().getStringList("Died")) {
            player.sendMessage(MessageManager.internalPlaceholders(player, killer, str));
        };
        // END | Adding effects to player

        // Create new armor stand and force the player to be a passenger of the armor stand.
        ArmorStand am2 = ArmorStandManager.createStand(player);
        am2.addPassenger(player);
    }

    public static void revivePlayer(Player target, Player reviver) {
        if (!reviver.hasPermission("DeathHolding.Revive")) return;

        clearEffects(target);
        ArmorStandManager.removeStands(target);
        target.setHealth(Main.config.getConfig().getDouble("Revival.Health"));
        dyingPlayers.remove(target.getUniqueId());

        target.teleport(target.getLocation().add(0, 0.5, 0));

        // START | PLAY SOUNDS
        Objects.requireNonNull(config.getConfigurationSection("Revival.SoundEffects")).getKeys(false).forEach(key -> {
            try {
                target.playSound(target.getLocation(), Sound.valueOf(config.getString("Revival.SoundEffects." + key)), 1, 1);
            } catch(Exception e) {
                Bukkit.getLogger().warning("Invalid Sound Effect in Config.yml");
            }
        });
        // END | PLAY SOUNDS

        // START | ADD POTION EFFECTS
        Objects.requireNonNull(config.getConfigurationSection("Revival.PotionEffects")).getKeys(false).forEach(key -> {
            String path = "Revival.PotionEffects." + key;

            if (config.getString(path + ".Type") == null) {
                Bukkit.getLogger().warning("Invalid Potion Effect in Config.yml");
                return;
            }

            PotionEffectType potionEffect = PotionEffectType.getByName(Objects.requireNonNull(config.getString(path + ".Type")));
            assert potionEffect != null;
            target.addPotionEffect(new PotionEffect(potionEffect, config.getInt(path + ".Duration" ) * 20, config.getInt(path + ".Strength") - 1));
        });
        // END | ADD POTION EFFECTS


        for (String str : Main.config.getConfig().getStringList("Revival.toTarget")) {
            target.sendMessage(MessageManager.internalPlaceholders(reviver, target, str));
        }

        for (String str : Main.config.getConfig().getStringList("Revival.toReviver")) {
            reviver.sendMessage(MessageManager.internalPlaceholders(reviver, target, str));
        }
    }

    public static void killPlayer(Player target) {
        target.setHealth(0);
        clearEffects(target);
        ArmorStandManager.removeStands(target);
        dyingPlayers.remove(target.getUniqueId());
    }

    private static void clearEffects(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.setGlowing(false);
    }

    static void deathTimer(Player player) {
        int time = Main.config.getConfig().getInt("Dying.TimeToRevive");
        ArmorStand am = ArmorStandManager.createStand(player, player.getLocation().add(0, 2, 0), null);
        am.setCustomNameVisible(true);
        new DyingTimer(time, Main.getPlugin()) {
            @Override
            public void count(int current) {
                if (!dyingPlayers.contains(player.getUniqueId())) {
                    ArmorStandManager.removeStands(player);
                    task.cancel();
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Time Remaining: " + current + " seconds!"));

                am.setCustomName(ColorManager.format(Main.config.getConfig().getString("Dying.Title") + " " + current).replace("%player_name%", player.getName()));

                if (current == 0) {
                    killPlayer(player);
                }

            }
        }.start();
    }
}
