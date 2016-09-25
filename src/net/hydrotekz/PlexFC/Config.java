package net.hydrotekz.PlexFC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Config {

	// Movies settings
	public static String moviesFrom = null;
	public static String moviesTo = null;
	public static int minMovieSize = 500; // File size in MB

	// TV shows settings
	public static String seriesFrom = null;
	public static String seriesTo = null;
	public static int minEpisodeSize = 100; // File size in MB

	// Common settings
	public static long scanInterval = 0; // In minutes
	public static boolean writeLog = true;
	public static boolean doCleanup = false; // Beta
	public static boolean autoUpdate = true;
	public static String videoExtensions = "avi,flv,mp4,mkv";

	// Other
	public static File configFile = new File(System.getProperty("user.dir") + File.separator + "settings.ser");

	public static void loadConfig(){
		if (configFile.exists()){
			Object[] o = null;
			try {
				FileInputStream fis = new FileInputStream(configFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				o = (Object[]) ois.readObject();
				ois.close();
				fis.close();
			} catch(Exception e) {
				Printer.log("Error occured while loading config file!");
				Printer.log(e);
			}
			if (o != null){
				moviesFrom = (String) o[0];
				moviesTo = (String) o[1];
				minMovieSize = (int) o[2];
				seriesFrom = (String) o[3];
				seriesTo = (String) o[4];
				minEpisodeSize = (int) o[5];
				scanInterval = (long) o[6];
				writeLog = (boolean) o[7];
				doCleanup = (boolean) o[8];
				autoUpdate = (boolean) o[9];
				videoExtensions = (String) o[10];
			}
		}
	}

	public static void saveConfig(){
		try {
			Object[] obj = {moviesFrom, moviesTo, minMovieSize, seriesFrom, seriesTo, minEpisodeSize, scanInterval, writeLog, doCleanup, autoUpdate, videoExtensions};
			FileOutputStream fos = new FileOutputStream(configFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.close();
			fos.close();
		} catch(Exception e) {
			Printer.log("Error occured while saving config file!");
			Printer.log(e);
		}
	}
}