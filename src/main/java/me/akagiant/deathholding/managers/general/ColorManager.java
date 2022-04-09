package me.akagiant.deathholding.managers.general;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorManager {

    public static String format(String msg) {
        if (!msg.equals("")) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

            String[] bukkitVer = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
            double converted = Double.parseDouble(bukkitVer[0] + "." + bukkitVer[1]);
            if (converted >= 1.16) {
                for (Matcher match = pattern.matcher(msg); match.find(); match = pattern.matcher(msg)) {
                    String color = msg.substring(match.start(), match.end());
                    msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                }
            }
            return ChatColor.translateAlternateColorCodes('&', msg);
        }
        return msg;
    }

    public static List<String> format(List<String> msg) {

        List<String> formatted = new ArrayList<>();

        for (String str : msg) {
            formatted.add(format(str));
        }

        return formatted;
    }

}
