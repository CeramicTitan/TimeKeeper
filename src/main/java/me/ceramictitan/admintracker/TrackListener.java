package me.ceramictitan.admintracker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.regex.Pattern;


public class TrackListener implements Listener {
    private AdminTracker plugin;
    TrackManager manager;

    public TrackListener(AdminTracker plugin) {
        this.plugin = plugin;
        this.manager = new TrackManager(plugin);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String name = p.getName();
        if (p.hasPermission("at.clock")) {
            event.setJoinMessage(null);
            String message = plugin.getConfig().getString("admin-join");
            message = message.replaceAll(Pattern.quote("{player}"),event.getPlayer().getName());
            message = ChatColor.translateAlternateColorCodes('&',message);
            Bukkit.getServer().broadcastMessage(message);
            if(!manager.playerFileExists(name)){
                try {
                    manager.createRecord(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                manager.addToCheckIn(name, manager.getTime());

        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        String name = p.getName();
        if (p.hasPermission("at.clock")) {
            manager.addToCheckout(name, manager.getTime());
            String checkin = manager.getCheckinTime(name);
            String checkout = manager.getCheckoutTime(name);
            int days = manager.getDays(checkin, checkout);
            int hours = manager.getHours(checkin, checkout);
            int minutes = manager.getMinutes(checkin, checkout);
            int seconds = manager.getSeconds(checkin, checkout);
            try {
                manager.logTime(name, checkin, checkout);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
            for (Player admins : Bukkit.getServer().getOnlinePlayers()) {
                if (admins.hasPermission("at.notify")) {
                    admins.sendMessage(name + " just logged out! They played for "
                      + days + " Days "
                      + hours + " Hours "
                      + minutes + " Minutes "
                      + seconds + " Seconds.");
                    break;
                }
            }
            System.out.print(name + " just logged out! They played for "
              + days + " Days "
              + hours + " Hours "
              + minutes + " Minutes "
              + seconds + " Seconds.");
            if(manager.alreadyInCheckInCache(name) && manager.alreadyInCheckoutCache(name)){
                manager.removeFromCheckin(name);
                manager.removeFromCheckout(name);
            }
        }
    }
}
