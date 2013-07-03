package me.ceramictitan.timekeeper;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.entity.Player;

public class ModTxt
{
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
	File File = new File(path, filename);
	FileReader fr = new FileReader(File);
	try{
	    int inChar;
	    while ((inChar = fr.read()) != -1) {
		txt = String.format(txt + "%c", new Object[] { Integer.valueOf(inChar) });
	    }
	}finally{
	    fr.close();
	}
	return txt;
    }

}