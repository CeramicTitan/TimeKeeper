package me.ceramictitan.timekeeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TimeKeeper extends JavaPlugin
{
    public final Logger logger = Logger.getLogger("Minecraft");
    public Map<String,String> log = new HashMap<String,String>(); // Format: <Player name>, Player name | Clock in: dd/mm/yyyy time | Clock Out: dd/mm/yyyy time | Total time logged in: Clock in - Clock Out. -> stored in logs.yml
    public TimeKeeper plugin;
    public String version;
    File pluginDir = new File("plugins/TimeKeeper");

    public TimekeeperListener TKListener = new TimekeeperListener(this);

    @Override
    public void onEnable()
    {
	PluginManager pm = getServer().getPluginManager();
	PluginDescriptionFile pdFile = getDescription();
	String ver = pdFile.getVersion();
	this.version = pdFile.getVersion();
	String enabled = " Time Keeper " + ver + " is enabled.";

	this.logger.info("[TK] " + enabled);

	if (!this.pluginDir.exists()) {
	    this.logger.log(Level.SEVERE, "[TK] Plugin directory not found! Creating...");
	    this.pluginDir.mkdir();
	    this.logger.log(Level.INFO, "[TK] Plugin directory created successfully.");
	}


	pm.registerEvents(this.TKListener, this);
    }

    @Override
    public void onDisable()
    {
	PluginDescriptionFile pdFile = getDescription();
	String ver = pdFile.getVersion();
	String disabled = " Time Keeper " + ver + " is now disbaled.";

	this.logger.info("[TK] " + disabled);
    }



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
	if (cmd.getName().equalsIgnoreCase("timekeeper")) {
	    if(args.length == 1){
		if ((args[0].equalsIgnoreCase("info")) || (args[0].equalsIgnoreCase("?"))) {
		    sender.sendMessage(ChatColor.GOLD + "[TK] " + ChatColor.YELLOW + "TimeKeeper v" + this.version + " by " + ChatColor.RED + "CeramicTitan");
		    if(sender.hasPermission("tk.check")){
			sender.sendMessage("/timekeeper check <flag> <player> to check different clock in modes!");
			sender.sendMessage(ChatColor.GREEN+"Applicable flags: -a: All clock in and clock out statuses; -l: For the latest clock in and clock out status.");
		    }
		    return true;
		}


	    }else if(args.length == 3){
		if(cmd.getName().equalsIgnoreCase("timekeeper")){
		    if(args[0].equalsIgnoreCase("check")){
			if(sender.hasPermission("tk.check") || sender.isOp()){
			    if(args[1].equalsIgnoreCase("-a")){
				Player p = getServer().getPlayerExact(args[2]);
				if(p !=null){
				    sender.sendMessage(p.getName()+" is online");
				    return true;
				}else{
				    File file = new File("plugins/TimeKeeper", args[2]+".txt");
				    if(file.exists()){
					try {
					    sender.sendMessage(ChatColor.DARK_PURPLE+"======="+ChatColor.DARK_AQUA+file.getName()+ChatColor.YELLOW+"(All)"+ChatColor.DARK_PURPLE+"=======");
					    sender.sendMessage(ModTxt.readFile("plugins/TimeKeeper", args[2]+".txt"));
					    return true;
					} catch (FileNotFoundException e) {
					    e.printStackTrace();
					} catch (IOException e) {
					    e.printStackTrace();
					}
				    }else{
					sender.sendMessage(args[1]+".txt doesn't exist!");
					return true;
				    }
				}



			    }else if(args[1].equalsIgnoreCase("-l")){
				Player p = getServer().getPlayerExact(args[2]);
				if(p !=null){
				    sender.sendMessage(p.getName()+" is online");
				    return true;
				}else{
				    File file = new File("plugins/TimeKeeper", args[2]+".txt");
				    if(file.exists()){
					try {
					    sender.sendMessage(ChatColor.DARK_PURPLE+"======="+ChatColor.DARK_AQUA+file.getName()+ChatColor.YELLOW+"(Latest)"+ChatColor.DARK_PURPLE+"=======");
					    ModTxt.readLatestEntry("plugins/TimeKeeper", args[2]+".txt", sender);
					    return true;
					} catch (FileNotFoundException e) {
					    e.printStackTrace();
					} catch (IOException e) {
					    e.printStackTrace();
					}
				    }else{
					sender.sendMessage(args[2]+".txt doesn't exist!");
					return true;
				    }
				}
			    }else{
				sender.sendMessage(ChatColor.RED+"Non applicable flag!");
				sender.sendMessage(ChatColor.DARK_AQUA+"Type /timekeeper info for applicable flags!");
			    }
			}
		    }
		}

	    }
	}
	return false;
    }
}
