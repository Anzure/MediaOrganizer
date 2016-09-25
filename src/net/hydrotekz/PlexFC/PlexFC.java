package net.hydrotekz.PlexFC;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javafx.application.Application;
import javafx.stage.Stage;
import net.hydrotekz.PlexFC.graphics.CriticalError;
import net.hydrotekz.PlexFC.graphics.GUI;
import net.hydrotekz.PlexFC.graphics.MainWindow;
import net.hydrotekz.PlexFC.handlers.ScanHandler;

public class PlexFC extends Application {

	public static double version = 5.0;
	public static String[] arguments;

	/*
	 * Advanced Plex Format Converter
	 * Made by WareManu (HydroTekZ)
	 */

	@Override
	public void start(Stage primaryWindow) throws Exception {
		MainWindow.startMainWindow(primaryWindow);
		// Automatic scanner
		if (Config.scanInterval > 0){
			Runnable r = new Runnable() {
				public void run() {
					Printer.log("");
					if (Config.scanInterval > 0)ScanHandler.autoScanner();
					Printer.log("Automatic scanner started!");
				}
			};
			new Thread(r).start();
		}
	}

	public static void main(String[] args){
		try {
			arguments = args;
			Printer.log("");
			Printer.log("##########################");
			Printer.log("#  PlexFC - Version " + version + "  #");
			Printer.log("##########################");
			Printer.log("");
			Printer.log("Root path: " + System.getProperty("user.dir"));
			Printer.log("");
			// Shutdown task
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					GUI.saveTextFields();
					Config.saveConfig();
					Updater.updateCheck();
					Updater.updateIfPossible();
					if (ScanHandler.isScanning) Printer.log("Waiting for scans to complete...");
					while(ScanHandler.isScanning){
						try {
							// Waiting...
							Thread.sleep(500);
						} catch (Exception e){};
					}
					Printer.log("Successfully shut down.");
				}
			});
			// Load configuration
			Config.loadConfig();
			// Open window
			launch(args);
		} catch (Exception e) {
			Printer.log("An unknown error was detected under startup!");
			Printer.log(e);
			try {
				CriticalError.title = new JLabel("An unknown error was detected while scanning, the program will attempt to update then shut down!");
				CriticalError.message = new JLabel("Short error message (check the log for extended information): " + e.toString());
				CriticalError wid = new CriticalError();
				wid.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				wid.setSize(853, 100);
				wid.setVisible(true);
			} catch (Exception ex){}
		}
	}
}