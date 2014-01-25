package me.ceramictitan.timekeeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminTracker extends JavaPlugin {
    // Format: <Player name>, Player name | Clock in: dd/mm/yyyy time | Clock Out: dd/mm/yyyy time | Total time logged in: Clock in - Clock Out. -> stored in logs.yml
    public AdminTracker plugin;
    private TrackManager manager;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        PluginDescriptionFile pdFile = getDescription();
        manager = new TrackManager(this);
        String ver = pdFile.getVersion();
        getConfig().addDefault("log-dump-size", 60);
        getConfig().options().copyDefaults(true);
        saveConfig();
        getLogger().info(" AdminTracker " + ver + " is enabled.");
        pm.registerEvents(new TrackListener(this), this);
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile pdFile = getDescription();
        String ver = pdFile.getVersion();
        getLogger().info(" AdminTracker " + ver + " is now disbaled.");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("admintracker")) {
            if (args.length == 1) {
                if ((args[0].equalsIgnoreCase("help"))) {
                    sender.sendMessage(ChatColor.YELLOW + "AdminTracker v" + this.getDescription().getVersion() + " by " + ChatColor.RED + "CeramicTitan");
                    if (sender.hasPermission("at.check")) {
                        sender.sendMessage("/adminwatch <flag> <player> to check different clock in modes!");
                        sender.sendMessage(ChatColor.GREEN + "Applicable flags: -a: All clock in and clock out statuses; -l: For the latest clock in and clock out status.");
                    }
                    return true;
                }

            }else if(args.length == 2){
                if(args[0].equalsIgnoreCase("purge")){
                    if(manager.playerFileExists(args[1])){
                        try {
                            manager.dumpLog(args[1]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
            } else if (args.length == 3) {
                        if (sender.hasPermission("at.check") || sender.isOp()) {
                            if (args[1].equalsIgnoreCase("-a")) {
                                Player p = getServer().getPlayerExact(args[2]);
                                if (p != null) {
                                    sender.sendMessage(args[2] + " is online");
                                    return true;
                                } else {
                                        if(manager.playerFileExists(args[2])){
                                            sender.sendMessage(ChatColor.DARK_PURPLE + "=======" + ChatColor.DARK_AQUA + p.getName() + ChatColor.YELLOW + "(All)" + ChatColor.DARK_PURPLE + "=======");
                                            for(String log : manager.getLog(args[2])){
                                                sender.sendMessage(log);
                                            }
                                        return true;

                                    } else {
                                        sender.sendMessage(args[1] + ".yml doesn't exist!");
                                        return true;
                                    }
                                }


                            } else if (args[1].equalsIgnoreCase("-l")) {
                                Player p = getServer().getPlayerExact(args[2]);
                                if (p != null) {
                                    sender.sendMessage(p.getName() + " is online");
                                    return true;
                                } else {
                                   if(manager.playerFileExists(args[2])){
                                            sender.sendMessage(ChatColor.DARK_PURPLE + "=======" + ChatColor.DARK_AQUA + args[2] + ChatColor.YELLOW + "(Latest)" + ChatColor.DARK_PURPLE + "=======");
                                            sender.sendMessage(manager.getLatestEntry(p.getName()));
                                            return true;
                                    } else {
                                        sender.sendMessage(p.getName() + ".yml doesn't exist!");
                                        return true;
                                    }
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Non applicable flag!");
                                sender.sendMessage(ChatColor.DARK_AQUA + "Type /admintracker info for applicable flags!");
                            }
                        }

            }
        }
        return false;
    }
}
