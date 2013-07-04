package me.ceramictitan.timekeeper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModTxt
{

    protected static void firstClockIn(File f, String string)
	    throws IOException
	    {
	try
	{
	    BufferedWriter edit = new BufferedWriter(new FileWriter(f, true));
	    edit.write("  -Clock In: " + string);
	    edit.flush();
	    edit.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	    }



    protected static void clockIn(File f, String string)
	    throws IOException
	    {
	try
	{
	    BufferedWriter edit = new BufferedWriter(new FileWriter(f, true));
	    edit.newLine();
	    edit.write("  -Clock In: " + string);
	    edit.flush();
	    edit.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	    }

    protected static void clockOut(File f, String string) throws IOException {
	try {
	    BufferedWriter edit = new BufferedWriter(new FileWriter(f, true));
	    edit.newLine();
	    edit.write("  -Clock Out: " + string);
	    edit.flush();
	    edit.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    protected static void clockTime(File f, String time, Player p) throws IOException {
	try { BufferedWriter edit = new BufferedWriter(new FileWriter(f, true));
	edit.newLine();
	edit.write(p.getName()+" was on for: " + time);
	edit.flush();
	edit.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }


    protected static String readFile(String path, String filename) throws FileNotFoundException, IOException
    {
	String txt = "";
	File file = new File(path, filename);
	FileInputStream in = new FileInputStream(file);
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	try{
	    int inChar;
	    while ((inChar = br.read()) != -1) {
		txt = String.format(txt + "%c", new Object[] { Integer.valueOf(inChar) });
	    }
	}finally{
	    br.close();
	}
	return txt;
    }
    protected static void readLatestEntry(String path, String filename, CommandSender sender) throws FileNotFoundException,IOException{
	ArrayList<String> lines = new ArrayList<String>();

	String tmp="";
	File file = new File(path, filename);
	FileInputStream in = new FileInputStream(file);
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	try{
	    while ((tmp = br.readLine()) != null) {
		lines.add(tmp);
	    }
	    for (int i = lines.size()-3; i < lines.size(); i++) {
		sender.sendMessage(lines.get(i));
	    }
	}finally{
	    br.close();
	}
    }

}