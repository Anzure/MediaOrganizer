package net.hydrotekz.PlexFC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Printer {

	public static void log(String message) {
		if (message == null) return;
		try {
			System.out.println(message);
			if (!Config.writeLog || message.length() < 1) return;
			File log = new File(System.getProperty("user.dir"));
			if (!log.exists()) {
				log.mkdirs();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(getMonth() + ".log", true));
			out.write(getDate() + ": " + message);
			out.newLine();
			out.close();
		} catch (Exception ev) {
			ev.printStackTrace();
		}
	}

	public static void log(Exception e){
		Printer.log("--- ERROR START ---");
		log(getErrorText(e));
		Printer.log("--- ERROR STOP ---");
	}

	public static String getErrorText(Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return(sw.toString());
	}

	private static String getMonth(){
		DateFormat dateFormat = new SimpleDateFormat("MM@yyyy");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

	private static String getDate(){
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
}