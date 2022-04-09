package me.akagiant.deathholding.managers;

import me.akagiant.deathholding.Main;
import me.akagiant.deathholding.managers.general.ColorManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.List;

public class RevivalManager {

    static FileConfiguration config = Main.config.getConfig();

    static ItemStack createRevivalItem(@Nullable Integer amount) {
        String path = "Revival.Item.custom-item";

        ItemStack revivalItem = new ItemStack(Material.valueOf(config.getString(path + ".type")));
        ItemMeta meta = revivalItem.getItemMeta();

        assert meta != null;

        String displayName = config.getString(path + ".displayName");
        List<String> lore = config.getStringList(path + ".lore");
        boolean isEnchanted = config.getBoolean(path + ".isEnchanted");

        if (isEnchanted) {
            meta.addEnchant(Enchantment.DURABILITY, 3, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.setDisplayName(ColorManager.format(displayName));
        meta.setLore(ColorManager.format(lore));

        revivalItem.setItemMeta(meta);

        if (amount != null) {
            revivalItem.setAmount(amount);
        }

        return revivalItem;
    }

    public static void consumeRevivalItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (player.getInventory().getItemInMainHand().getAmount() == 1) {
            player.getInventory().getItem(EquipmentSlot.HAND).setAmount(0);
        } else {
            player.getInventory().getItemInMainHand().setAmount(item.getAmount() - 1);
        }

    }

    public static void giveRevivalItem(Player sender, Player player, int amount) {

        if (player.getInventory().firstEmpty() == -1) {
            sender.sendMessage("Inventory Full");
            return;
        }

        boolean isCustom = Main.config.getConfig().getBoolean("Revival.Item.isCustom");
        if (isCustom)
            player.getInventory().addItem(createRevivalItem(amount));
        else
            player.getInventory().addItem(new ItemStack(Material.valueOf(Main.config.getConfig().getString("Revival.Item.default-item")), amount));

    }

}
