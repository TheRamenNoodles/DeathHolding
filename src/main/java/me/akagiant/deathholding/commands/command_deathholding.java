package me.akagiant.deathholding.commands;

import me.akagiant.deathholding.Main;
import me.akagiant.deathholding.managers.RevivalManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class command_deathholding implements CommandExecutor, TabCompleter {

    Main plugin;

    public command_deathholding(Main instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, String cmdName, String[] args) {

        if (cmdName.equalsIgnoreCase(cmd.getName())) {
            if (!(sender instanceof Player)) {
                return true;
            }


            // USAGE: /dh reload
            // USAGE: /dh give %player% %amt%

            Player player = (Player) sender;

            if (args.length == 0) {
                sendUsage(player);
                return false;
            }

            switch (args[0]) {
                case "clear":
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=minecraft:armor_stand, tag=deathholding]");
                    break;
                case "give":
                    if (args.length >= 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("Invalid Target");
                            return false;
                        } else
                            RevivalManager.giveRevivalItem(player, player, args[2] != null ? Integer.parseInt(args[2]) : 1);
                        break;
                    }
                    giveUsage(player);
                    break;
                case "reload":
                    try {
                        Main.config.reloadConfig();
                        player.sendMessage("Config.yml reloaded!");
                    } catch(Exception e) {
                        player.sendMessage("Error Reloading the Config.yml");
                        Bukkit.getLogger().severe("Error Reloading Config.yml");
                        e.printStackTrace();
                    }
                    break;
            }

            return true;
        }
        return false;
    }


    void sendUsage(Player player) {
        player.sendMessage("-------------");
        player.sendMessage("Death Holding Usage");
        player.sendMessage(" ");
        player.sendMessage("Reload: /dh reload" );
        player.sendMessage("Clear: /dh clear" );
        player.sendMessage("Give: /dh give <player> (amount)");
        player.sendMessage("   Required: <player> | The target of this command ");
        player.sendMessage("   Optional: (amount) | The amount of revival items \n   to give to a player ");
        player.sendMessage("-------------");
    }

    void giveUsage(Player player) {
        player.sendMessage("-------------");
        player.sendMessage("Death Holding Usage");
        player.sendMessage(" ");
        player.sendMessage("Give: /dh give <player> (amount)");
        player.sendMessage("   Required: <player> | The target of this command ");
        player.sendMessage("   Optional: (amount) | The amount of revival items \n   to give to a player ");
        player.sendMessage("-------------");
    }


    List<String> arguments = new ArrayList<>();
    List<String> result = new ArrayList<>();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            arguments.add("reload");
            arguments.add("give");
            arguments.add("clear");

            for (String arg : arguments) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(arg);
                }
            }
            return result;
        }

        if (args.length == 2 && args[0].equals("reload") || args[0].equals("clear")) {
            arguments.clear();
            result.clear();
            return result;
        }

        return null;
    }
}

