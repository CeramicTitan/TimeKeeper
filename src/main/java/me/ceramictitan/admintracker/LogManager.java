package me.ceramictitan.admintracker;

import org.bukkit.configuration.InvalidConfigurationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogManager {
    private TrackManager manager;
    private AdminTracker plugin;
    LogManager(AdminTracker plugin){
        this.plugin=plugin;
        manager = plugin.getManager();
    }

    protected boolean playerFileExists(String playerName) {
        File dir = new File("plugins/AdminTracker/Users");
        File file = new File(dir, playerName + ".txt");
        return file.exists();
    }
    protected File getPlayerFile(String name){
        if(playerFileExists(name)){
            File dir = new File("plugins/AdminTracker/Users");
            File file = new File(dir, name + ".txt");
            return file;
        }
        return null;
    }
    protected void logToFile(String name, String checkin, String checkout) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(getPlayerFile(name)), 32768);
        StringBuilder sb = new StringBuilder();
        sb = sb.append(name).append("/")
          .append("Checkin: ")
          .append(checkin).append("/")
          .append("Checkout: ")
          .append(checkout).append("/")
          .append("Total time online: ")
          .append(manager.getDays(checkin, checkout)).append(" Days ")
          .append(manager.getHours(checkin, checkout))
          .append(" Hours ").append(manager.getMinutes(checkin, checkout))
          .append(" Minutes ")
          .append(manager.getSeconds(checkin, checkout))
          .append(" Seconds");
        out.write(sb.toString());
        out.newLine();

    }
    protected void createRecord(String playerName) throws IOException {
        File dir = new File("plugins/AdminTracker/Users");
        File file = new File(dir, playerName + ".txt");
        dir.mkdir();
        file.createNewFile();

    }
    protected List<String> getLog(String name) throws IOException, InvalidConfigurationException {
        List<String> temp = new ArrayList<String>();
        File file = getPlayerFile(name);
        BufferedReader buf = new BufferedReader(new FileReader(file));
        String line = null;
        while((line = buf.readLine()) != null){
            temp.add(line);
        }
        return temp;
    }

}
