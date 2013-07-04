package me.ceramictitan.timekeeper;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TimekeeperListener
implements Listener
{
    public TimeKeeper plugin;
    long clockinmilis;

    public TimekeeperListener(TimeKeeper instance)
    {
	this.plugin = instance;
    }

    @EventHandler
    public void clockIn(PlayerJoinEvent event)
	    throws IOException
	    {
	String pName = event.getPlayer().getName();
	Player player = event.getPlayer();

	if (player.hasPermission("tk.clock")) {
	    String path = "plugins/TimeKeeper";
	    SimpleDateFormat clockTime = new SimpleDateFormat("d/MMM/yyyy hh:mmaa");
	    long clockmilis = System.currentTimeMillis();
	    this.clockinmilis = clockmilis;
	    File pFile = new File("plugins/TimeKeeper", pName + ".txt");
	    if (pFile.exists()) {
		ModTxt.clockIn(pFile, clockTime.format(new Date()));
	    }
	    else if (!pFile.exists()) {
		this.plugin.getLogger().log(Level.INFO, "File " + pFile + " doesn't exist! Creating file...");
		pFile = new File(path, pName + ".txt");
		pFile.createNewFile();
		ModTxt.firstClockIn(pFile, clockTime.format(new Date()));
		this.plugin.getLogger().log(Level.INFO, "File " + pFile + " created, log has been updated.");
	    }
	}

	if ((player.hasPermission("tk.announce.mod"))) {
	    this.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[TK] " + ChatColor.RED + pName + ChatColor.YELLOW + " is here. Need help? Ask them!");
	}
	    }

    @EventHandler
    public void clockOut(PlayerQuitEvent event) throws IOException {
	String pName = event.getPlayer().getName();
	Player player = event.getPlayer();
	if (player.hasPermission("tk.clock")) {
	    String path = "plugins/TimeKeeper";
	    long calmilis = System.currentTimeMillis();
	    SimpleDateFormat clockTime = new SimpleDateFormat("d/MMM/yyyy hh:mmaa");
	    long miliduration = calmilis - this.clockinmilis; 
	    if (miliduration < 0L)
	    {
		miliduration = TimeUnit.HOURS.toMillis(24L) + miliduration;
	    }
	    String duration = String.format("%d hours, %d minute(s), %d second(s)", new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toHours(miliduration)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(miliduration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(miliduration))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(miliduration)%60)});
	    File pFile = new File(path, pName + ".txt");
	    if (pFile.exists()) {
		ModTxt.clockOut(pFile, clockTime.format(new Date()));
		ModTxt.clockTime(pFile, duration, player);
	    } else if (!pFile.exists()) {
		this.plugin.getLogger().log(Level.SEVERE, "File " + pFile + " doesn't exist! Creating file...");
		pFile = new File(path, pName + ".txt");
		pFile.createNewFile();
	    }
	}
    }
}
