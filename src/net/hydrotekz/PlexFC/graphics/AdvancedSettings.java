package net.hydrotekz.PlexFC.graphics;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import net.hydrotekz.PlexFC.Config;

public class AdvancedSettings {
	
	public static BorderPane setupSettings(Button dashboardButton, Button settingsButton){
		// GridPane
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 0, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);
		// HBox
		HBox topMenu = new HBox();
		topMenu.setPadding(new Insets(10, 10, 0, 10));
		HBox.setMargin(dashboardButton, new Insets(0, 5, 0, 5));
		HBox.setMargin(settingsButton, new Insets(0, 5, 0, 5));
		topMenu.getChildren().addAll(dashboardButton, settingsButton);
		// Items
		Label label1 = new Label("Write log:");
		Label label2 = new Label("Do cleanup:");
		Label label3 = new Label("Auto update:");
		Label label4 = new Label("Video extensions:");
		Label label5 = new Label("Minimum movie size:");
		Label label6 = new Label("Minimum episode size:");
		Label label7 = new Label("MB");
		Label label8 = new Label("MB");
		// Settings
		// Write log
		ChoiceBox<String> writeLog = new ChoiceBox<>();
		writeLog.getItems().addAll("true", "false");
		writeLog.setValue(String.valueOf(Config.writeLog));
		writeLog.setOnAction(e -> Config.writeLog = Boolean.parseBoolean(writeLog.getValue()));
		// Do cleanup
		ChoiceBox<String> doCleanup = new ChoiceBox<>();
		doCleanup.getItems().addAll("true", "false");
		doCleanup.setValue(String.valueOf(Config.doCleanup));
		doCleanup.setOnAction(e -> Config.doCleanup = Boolean.parseBoolean(doCleanup.getValue()));
		// Auto update
		ChoiceBox<String> autoUpdate = new ChoiceBox<>();
		autoUpdate.getItems().addAll("true", "false");
		autoUpdate.setValue(String.valueOf(Config.autoUpdate));
		autoUpdate.setOnAction(e -> Config.autoUpdate = Boolean.parseBoolean(autoUpdate.getValue()));
		// Video extensions
		GUI.videoExtensions = new TextField(Config.videoExtensions);
		// Minimum movie size
		GUI.minMovieSize = new TextField(String.valueOf(Config.minMovieSize));
		// Minimum episode size
		GUI.minEpisodeSize = new TextField(String.valueOf(Config.minEpisodeSize));
		// Placements
		GridPane.setConstraints(label1, 0, 2);
		GridPane.setConstraints(writeLog, 1, 2);
		GridPane.setConstraints(label2, 0, 4);
		GridPane.setConstraints(doCleanup, 1, 4);
		GridPane.setConstraints(label3, 0, 6);
		GridPane.setConstraints(autoUpdate, 1, 6);
		GridPane.setConstraints(label4, 0, 8);
		GridPane.setConstraints(label5, 0, 10);
		GridPane.setConstraints(label6, 0, 12);
		GridPane.setConstraints(label7, 2, 10);
		GridPane.setConstraints(label8, 2, 12);
		GridPane.setConstraints(GUI.videoExtensions, 1, 8);
		GridPane.setConstraints(GUI.minMovieSize, 1, 10);
		GridPane.setConstraints(GUI.minEpisodeSize, 1, 12);
		// Return
		grid.getChildren().addAll(label1, label2, label3, label4, label5, label6, label7, label8, GUI.minMovieSize, GUI.minEpisodeSize, GUI.videoExtensions, writeLog,
				doCleanup, autoUpdate);
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topMenu);
		borderPane.setLeft(grid);
		return borderPane;
	}
}