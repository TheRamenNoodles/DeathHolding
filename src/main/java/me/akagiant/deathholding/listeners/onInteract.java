package me.akagiant.deathholding.listeners;

import me.akagiant.deathholding.Main;
import me.akagiant.deathholding.managers.CooldownManager;
import me.akagiant.deathholding.managers.DyingManager;
import me.akagiant.deathholding.managers.RevivalManager;
import me.akagiant.deathholding.managers.general.MessageManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class onInteract implements Listener {

    private final CooldownManager cooldownManager = new CooldownManager();
    FileConfiguration config = Main.config.getConfig();

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player && e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {
            String path = "Revival.Item";

            boolean useCustomItem = config.getBoolean(path + ".isCustom");

            String material = useCustomItem ? config.getString(path + ".custom-item.type") : config.getString(path + ".default-item");
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();


            if (useCustomItem && item.getType() == Material.valueOf(material)) {
                ItemMeta meta = item.getItemMeta();
                if (meta == null) return;

                String requiredItemName = config.getString(path + ".displayName");
                List<String> requiredItemLore = config.getStringList(path + ".lore");

                if (meta.getDisplayName().equals(requiredItemName) && Objects.equals(meta.getLore(), requiredItemLore)) {
                    execute((Player) e.getRightClicked(), e.getPlayer());
                }
            } else {
                if (item.getType().equals(Material.valueOf(material))) {
                    execute((Player) e.getRightClicked(), e.getPlayer());
                }
            }
        }
    }

    public void execute(Player target, Player reviver) {
        if (DyingManager.dyingPlayers.contains(target.getUniqueId())) {
            long timeLeft = System.currentTimeMillis() - cooldownManager.getCooldwon(reviver.getUniqueId());
            if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.COOLDOWN) {
                RevivalManager.consumeRevivalItem(reviver);
                DyingManager.revivePlayer(target, reviver);
                cooldownManager.setCooldown(reviver, System.currentTimeMillis());
            } else {
                for (String str : Main.config.getConfig().getStringList("Revival.Cooldown.Message")) {
                    reviver.sendMessage(MessageManager.internalPlaceholders(reviver, null, str));
                }
            }
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player && e.getDismounted().getType().equals(EntityType.ARMOR_STAND) && DyingManager.dyingPlayers.contains(Objects.requireNonNull(((Player) e.getEntity()).getPlayer()).getUniqueId())) e.setCancelled(true);
    }

}
