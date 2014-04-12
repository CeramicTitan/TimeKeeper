package me.ceramictitan.admintracker;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TrackManager {
    AdminTracker plugin;
    Map<String, String> checkincache = new HashMap<String, String>();
    Map<String, String> checkoutcache = new HashMap<String, String>();
    Map<String, List<String>> log = new HashMap<String, List<String>>();
    private LogManager logManager;
    public TrackManager(AdminTracker plugin){
        this.plugin=plugin;
        this.logManager = plugin.getLogManager();
    }

    protected String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date now = new Date();
        String time = sdf.format(now);
        return time;

    }

    protected int getDays(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        int days = 0;
        try {
            Date checkin = sdf.parse(time1);
            Date checkout = sdf.parse(time2);
            long difference = checkout.getTime() - checkin.getTime();
            days = (int) (difference / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    protected int getHours(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        int hours = 0;
        try {
            Date checkin = sdf.parse(time1);
            Date checkout = sdf.parse(time2);
            long difference = checkout.getTime() - checkin.getTime();
            hours = (int) (difference / (60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hours;
    }

    protected int getMinutes(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        int mins = 0;
        try {
            Date checkin = sdf.parse(time1);
            Date checkout = sdf.parse(time2);
            long difference = checkout.getTime() - checkin.getTime();
            mins = (int) (difference / (60 * 1000) % 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mins;

    }

    protected int getSeconds(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        int secs = 0;
        try {
            Date checkin = sdf.parse(time1);
            Date checkout = sdf.parse(time2);
            long difference = checkout.getTime() - checkin.getTime();
            secs = (int) (difference / 1000 % 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return secs;
    }

    protected void addToCheckIn(String name, String time) {
        checkincache.put(name, time);
    }

    protected void addToCheckout(String name, String time) {
        checkoutcache.put(name, time);
    }
    protected void removeFromCheckin(String name){
        checkincache.remove(name);
    }
    protected void removeFromCheckout(String name){
        checkoutcache.remove(name);
    }

    protected boolean alreadyInCheckInCache(String name) {
        return checkincache.containsKey(name);
    }

    protected boolean alreadyInCheckoutCache(String name) {
        return checkoutcache.containsKey(name);
    }

    protected Logger getLogger() {
        return Bukkit.getLogger();
    }

    protected String getCheckoutTime(String name) {
        return checkoutcache.get(name);
    }

    protected String getCheckinTime(String name) {
        return checkincache.get(name);

    }

    protected List<String> getLog(String name) throws IOException, InvalidConfigurationException {
        List<String> temp = new ArrayList<String>();
        File file = logManager.getPlayerFile(name);
        BufferedReader buf = new BufferedReader(new FileReader(file));
        String line = null;
        while((line = buf.readLine()) != null){
            temp.add(line);
        }
        return temp;
    }

    protected boolean hasLog(String name) {
        return getLogCache().containsKey(name);

    }

    protected void clearCache() {
        checkoutcache.clear();
        checkincache.clear();
        getLogCache().clear();
    }

    protected Map<String, List<String>> getLogCache() {
        return log;
    }

}
