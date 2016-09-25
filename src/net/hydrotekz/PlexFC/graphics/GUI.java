package net.hydrotekz.PlexFC.graphics;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.hydrotekz.PlexFC.Config;
import javafx.scene.control.TextField;

public class GUI {

	public static TextField showsOutPath, showsInPath, moviesOutPath, moviesInPath, videoExtensions, minMovieSize, minEpisodeSize;
	public static Stage window;
	public static Scene dashboard, settings;
	public static Button dashboardButton1, dashboardButton2, settingsButton1, settingsButton2, scanButton;
	public static StringProperty currentJob = new Label("").textProperty();
	public static boolean windowOpen = false;
	public static DoubleProperty progress = new SimpleDoubleProperty();
	public static BooleanProperty progressVisible = new SimpleBooleanProperty();

	public static void autoSaver(){
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (windowOpen) saveTextFields();
			}
		}, 1, 1, TimeUnit.SECONDS);
	}

	public static void saveTextFields(){
		if (showsOutPath != null) Config.seriesFrom = showsOutPath.getText();
		if (showsInPath != null) Config.seriesTo = showsInPath.getText();
		if (moviesOutPath != null) Config.moviesFrom = moviesOutPath.getText();
		if (moviesInPath != null) Config.moviesTo = moviesInPath.getText();
		if (videoExtensions != null) Config.videoExtensions = videoExtensions.getText();
		if (minMovieSize != null) Config.minMovieSize = Integer.parseInt(minMovieSize.getText());
		if (minEpisodeSize != null) Config.minEpisodeSize = Integer.parseInt(minEpisodeSize.getText());
	}
}