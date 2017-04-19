package net.hydrotekz.PlexFC.handlers;

import java.io.File;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import javafx.application.Platform;
import net.hydrotekz.PlexFC.Config;
import net.hydrotekz.PlexFC.Printer;
import net.hydrotekz.PlexFC.Utils;
import net.hydrotekz.PlexFC.graphics.GUI;
import net.hydrotekz.PlexFC.graphics.Popup;
import net.hydrotekz.PlexFC.scanners.MoviesScanner;
import net.hydrotekz.PlexFC.scanners.SeriesScanner;

public class ScanHandler {

	public static boolean isScanning = false;
	private static long secondsPassed = 0;

	public static void autoScanner(){
		if (Config.scanInterval > 0){
			Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				@Override
				public void run() {
					secondsPassed+=8;
					if (secondsPassed >= Config.scanInterval && !isScanning){
						scan(false);
					} else {
						autoScanner();
					}
				}
			}, 8, TimeUnit.SECONDS);
		} else secondsPassed = 0;
	}

	public static void scan(boolean manual){
		try {
			Runnable r = new Runnable() {
				public void run() {
					try {
						GUI.saveTextFields();
						if (isScanning){
							Printer.log("Tried to start scan while a scan was already running, please wait for the current scan to complete instead.");
							Platform.runLater(() -> {
								Popup.error("Scan already running!", "Please wait for current scan to complete.");
							});
							return;
						}
						isScanning = true;

						Printer.log("Scan started.");
						Platform.runLater(() -> {
							GUI.progressVisible.set(false);
							GUI.progress.set(0);
							GUI.currentJob.set("Checking files");
						});

						// Checking for TV shows...
						Platform.runLater(() -> {
							GUI.progressVisible.set(false);
							GUI.progress.set(0);
							GUI.currentJob.set("");
						});
						boolean shows = false;
						File seriesFrom = null;
						File seriesTo = null;
						if (Config.seriesFrom != null && Config.seriesTo != null && Config.seriesFrom.length() > 0 && Config.seriesTo.length() > 0){
							seriesFrom = new File(Config.seriesFrom);
							seriesTo = new File(Config.seriesTo);
							if (!seriesFrom.exists() || !seriesFrom.isDirectory() || !seriesTo.exists() || !seriesTo.isDirectory()){
								Printer.log("TV shows scanning cancelled, please create all necessary folders!");
								Platform.runLater(() -> {
									GUI.progressVisible.set(false);
									GUI.progress.set(0);
									GUI.currentJob.set("Standing by.");
									Popup.error("TV show scanning was cancelled.", "To fix this, please create all necessary folders!");
								});
								isScanning = false;
								return;
							}
							scanElement(seriesFrom);
							shows = true;

						} else {
							Printer.log("TV show scanning seems to be off.");
						}

						// Checking for movies...
						boolean movies = false;
						File moviesFrom = null;
						File moviesTo = null;
						if (Config.moviesFrom != null && Config.moviesTo != null && Config.moviesFrom.length() > 0 && Config.moviesTo.length() > 0){
							moviesFrom = new File(Config.moviesFrom);
							moviesTo = new File(Config.moviesTo);
							if (!moviesFrom.exists() || !moviesFrom.isDirectory() || !moviesTo.exists() || !moviesTo.isDirectory()){
								Printer.log("Movies scanning cancelled, please create all necessary folders!");
								Platform.runLater(() -> {
									GUI.progressVisible.set(false);
									GUI.progress.set(0);
									GUI.currentJob.set("Standing by.");
									Popup.error("Movie scanning was cancelled.", "To fix this, please create all necessary folders!");
								});
								isScanning = false;
								return;
							}
							scanElement(moviesFrom);
							movies = true;

						} else {
							Printer.log("Movie scanning seems to be off.");
						}

						// Warning
						if (!shows && !movies){
							Printer.log("Neither TV shows or movies to scan for!");
							Popup.warn("Scan aborted!", "Neither TV shows or movies to scan for.");
							Platform.runLater(() -> {
								GUI.progressVisible.set(false);
								GUI.progress.set(0);
								GUI.currentJob.set("Standing by.");
							});
							isScanning = false;
							return;
						}

						// Scanning...
						Platform.runLater(() -> {
							GUI.progressVisible.set(false);
							GUI.progress.set(0);
							GUI.currentJob.set("Scanning files");
						});
						for (File file : ToDoHandler.toCheck){
							if (shows) SeriesScanner.scanFile(file);
							if (movies) MoviesScanner.scanFile(file);
						}
						ToDoHandler.toCheck.clear();

						// Move video files...
						if (!ToDoHandler.toMove.isEmpty()){
							Printer.log("");
							Printer.log("Moving video files... (may take a while)");
							int i = 0;
							for (Entry<File, File> e : ToDoHandler.toMove.entrySet()){
								// Check if it still exists
								if (!Utils.refresh(e.getKey()).exists()) continue;

								// Check for duplicate
								if (!DupeHandler.checkForDupe1(Utils.refresh(e.getKey()), Utils.refresh(e.getValue()))
										|| !DupeHandler.checkForDupe2(Utils.refresh(e.getKey()), Utils.refresh(e.getValue()))){
									continue;
								}

								// Move video file
								final double progress = (double) ((double) i / (double) ToDoHandler.toMove.entrySet().size());
								final int current = i;
								Platform.runLater(() -> {
									GUI.progressVisible.set(true);
									GUI.progress.set(progress);
									GUI.currentJob.set("Moving files (" + current + "/" + ToDoHandler.toMove.entrySet().size() + ")");
								});
								if (!Utils.isLocked(e.getKey())) FileUtils.moveFile(e.getKey(), e.getValue());
								else {
									FileUtils.copyFile(e.getKey(), e.getValue());
									ToDoHandler.toDelete.add(e.getKey());
								}
								i++;
							}
							ToDoHandler.toMove.clear();
							Printer.log("Moving complate!");
						}

						// Cleaning up...
						if (Config.doCleanup){
							int i = 0;
							if (movies){
								i += CleanupHandler.performCleanup(moviesFrom);
								i += CleanupHandler.deleteUnwantedFiles(moviesFrom);
								i += CleanupHandler.deleteUnwantedFiles(moviesTo);
							}
							if (shows){
								i += CleanupHandler.performCleanup(seriesFrom);
								i += CleanupHandler.deleteUnwantedFiles(seriesFrom);
								i += CleanupHandler.deleteUnwantedFiles(seriesTo);
							}

							if (i > 0) Printer.log("A total of " + i + " elements was deleted while cleaning up.");
							ToDoHandler.toDelete.clear();
						}

						// Finished
						Printer.log("Scan finished!");
						Platform.runLater(() -> {
							GUI.progressVisible.set(false);
							GUI.progress.set(0);
							GUI.currentJob.set("Standing by.");
							if (manual) Popup.info("Scan finished!", "Manual scan is now done.");
						});
						isScanning = false;

					} catch (Exception e) {
						Printer.log("An unknown error was detected while scanning!");
						Printer.log(e);
						Platform.runLater(() -> {
							Popup.error("A fatal error was detected while scanning, the program will attempt to update then shut down!", "Short error message"
									+ " (check the log for extended information): " + e.toString());
							System.exit(0);
						});
					}

				}
			};
			new Thread(r).start();

		} catch (Exception e) {
			Printer.log("An unknown error was detected while scanning!");
			Printer.log(e);
			Platform.runLater(() -> {
				Popup.error("A fatal error was detected while scanning, the program will attempt to update then shut down!", "Short error message"
						+ " (check the log for extended information): " + e.toString());
			});
			System.exit(0);
		}
	}

	private static void scanElement(File file){
		if (file.isFile() && !file.isDirectory() && Utils.isVideo(file)){
			ToDoHandler.toCheck.add(file);

		} else if (file.isDirectory()){
			for (File f : file.listFiles()){
				scanElement(Utils.refresh(f));
			}
		}
	}

}