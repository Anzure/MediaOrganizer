package net.hydrotekz.PlexFC;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class Updater {

	private static double latestVersion = 0.00;
	private static String updateLink = "https://dl.dropboxusercontent.com/u/77919443/Applications/PlexFC/";

	public static void updateCheck(){
		double latest = latestVersion;
		updateLatestVersion();
		if (latestVersion > latest){
			Printer.log("Update found! Current version: " + PlexFC.version + " Latest version: " + latestVersion);
		}
	}

	public static void updateIfPossible(){
		try {
			if (Config.autoUpdate && latestVersion > PlexFC.version){
				updateApplication(latestVersion);
			}
		} catch (Exception e){
			Printer.log("Error detected while updating!");
			Printer.log(e);
		}
	}

	private static void updateLatestVersion(){
		double latest = 0.00;
		try {
			URL url = new URL(updateLink + "Update.txt");

			Scanner s = new Scanner(url.openStream());
			StringBuilder text = new StringBuilder();
			while (s.hasNext()){
				String append = s.next();
				if (s.hasNext()) append += " ";
				text.append(append);
			}
			s.close();
			String output = text.toString();
			if (output != null && !output.isEmpty() && output.length() > 2){
				latest = Double.parseDouble(output);
			}
		} catch(Exception ex) {}
		if (latest > PlexFC.version) latestVersion = latest;
	}

	private static void updateApplication(double version){
		try {
			Printer.log("Downloading update...");
			URL url = new URL(updateLink + "PlexFCv" + version + ".jar");
			HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
			https.setRequestMethod("GET");
			https.connect();
			int code = https.getResponseCode();
			if (code == 200){
				ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				File file = new File(System.getProperty("user.dir") + File.separator + "PlexFC.jar");
				FileOutputStream fos = new FileOutputStream(file);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				https.disconnect();
				fos.close();
				rbc.close();
				Printer.log("Update downloaded! A restart is required to install the update.");
				Printer.log("");
				PlexFC.version = version;

			} else {
				Printer.log("Update aborted, unsafe response code: " + code);
			}
		} catch (Exception e){
			Printer.log("Error detected, failed to download update!");
			Printer.log(e);
		}
	}
}