package me.ceramictitan.timekeeper;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public TrackManager(AdminTracker plugin){
        this.plugin=plugin;
    }

    protected void createRecord(String playerName) throws IOException {
        File dir = new File("plugins/AdminTracker");
        File file = new File(dir, playerName + ".yml");
        dir.mkdir();
        file.createNewFile();

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

    protected boolean playerFileExists(String playerName) {
        File dir = new File("plugins/AdminTracker");
        File file = new File(dir, playerName + ".yml");
        return file.exists();
    }

    protected FileConfiguration loadDataFile(String playerName) throws IOException, InvalidConfigurationException {
        FileConfiguration data = new YamlConfiguration();
        File dir = new File("plugins/AdminTracker");
        File file = new File(dir, playerName + ".yml");
        data.load(file);
        return data;

    }

    protected void saveDataFile(String playerName) throws IOException {
        FileConfiguration data = new YamlConfiguration();
        File dir = new File("plugins/AdminTracker");
        File file = new File(dir, playerName + ".yml");
        data.set("log", getLog(playerName));
        data.save(file);

    }
    protected void dumpLog(String name) throws IOException {
        FileConfiguration data = new YamlConfiguration();
        File dir = new File("plugins/AdminTracker");
        File file = new File(dir, name + ".yml");
        List<String> temp = new ArrayList<String>();
        data.set("log", temp);
        data.save(file);

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

    protected void log(Level level, String error) {
        getLogger().log(level, error);
    }

    protected String getCheckoutTime(String name) {
        return checkoutcache.get(name);
    }

    protected String getCheckinTime(String name) {
        return checkincache.get(name);

    }

    protected void logTime(String name, String checkin, String checkout) throws IOException, InvalidConfigurationException {
        FileConfiguration data = loadDataFile(name);
        List<String> temp = data.getStringList("log");
        if(data == null || temp == null || temp.size() >=  plugin.getConfig().getInt("log-dump-size",60)){
            if(getLog(name) != null){
                temp = getLog(name);
            }else{
                temp = new ArrayList<String>();
            }
        }
        StringBuilder sb = null;
        sb = sb.append(name).append(" | ")
          .append("Checkin:")
          .append(checkin).append(" | ")
          .append("Checkout:")
          .append(checkout).append(" | ")
          .append("Total time online:")
          .append(getDays(checkin, checkout)).append(" Days ")
          .append(getHours(checkin, checkout))
          .append(" Hours ").append(getMinutes(checkin, checkout))
          .append(" Minutes ")
          .append(getSeconds(checkin, checkout))
          .append(" Seconds ");
        temp.add(sb.toString());
        log.put(name, temp);
    }

    protected List<String> getLog(String name) {
        return log.get(name);
    }

    protected boolean hasLog(String name) {
        return log.containsKey(name);

    }

    protected String getSplit(int index, List<String> list) {
        String temp = null;
        for (String string : list) {
            String[] split = string.split("\\s+\\|\\s+");
            temp = split[index];
        }
        return temp;
    }

    protected void clearCache() {
        checkincache.clear();
        checkincache.clear();
        log.clear();
    }

    protected Map<String, List<String>> getLogCache() {
        return log;
    }
    protected String getLatestEntry(String name){
        return getLog(name).get(getLog(name).size()-1);
    }

}
