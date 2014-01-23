package me.ceramictitan.timekeeper;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeManager {
    FileConfiguration data = new YamlConfiguration();

    protected  void createRecord(String playerName) throws IOException {
        File dir = new File("plugins/TimeKeeper");
        File file = new File(dir,playerName+".yml");
        dir.mkdir();
        file.createNewFile();

    }
    protected String clockIn(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date now = new Date();
        String clockin = sdf.format(now);
        return clockin;

    }
    protected String clockOut(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date now = new Date();
        String clockout = sdf.format(now);
        return clockout;


    }
    protected int getDays(String time1, String time2){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        int days = 0;
        try {
            Date checkin = sdf.parse(time1);
            Date checkout = sdf.parse(time2);
            long difference = checkout.getTime() - checkin.getTime();
            days =  (int) (difference / (1000*60*60*24));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }
    protected int getHours(String time1, String time2){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        int hours = 0;
        try {
            Date checkin = sdf.parse(time1);
            Date checkout = sdf.parse(time2);
            long difference = checkout.getTime() - checkin.getTime();
            hours= (int)(difference / (60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hours;
    }
    protected int getMinutes(String time1, String time2){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        int mins = 0;
        try {
            Date checkin = sdf.parse(time1);
            Date checkout = sdf.parse(time2);
            long difference = checkout.getTime() - checkin.getTime();
            mins = (int)(difference / (60 * 1000) % 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mins;

    }
    protected int getSeconds(String time1, String time2){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        int secs = 0;
        try {
            Date checkin = sdf.parse(time1);
            Date checkout = sdf.parse(time2);
            long difference = checkout.getTime() - checkin.getTime();
            secs = (int)(difference/1000%60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    return secs;
    }

    protected boolean playerFileExists(String playerName){
        File dir = new File("plugins/TimeKeeper");
        File file = new File(dir,playerName+".yml");
        return file.exists();
    }
    protected FileConfiguration loadDataFile(String playerName) throws IOException, InvalidConfigurationException {
        File dir = new File("plugins/TimeKeeper");
        File file = new File(dir,playerName+".yml");
        data.load(file);
        return data;

    }
}
