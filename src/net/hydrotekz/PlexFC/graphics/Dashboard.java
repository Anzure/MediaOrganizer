package net.hydrotekz.PlexFC.graphics;

import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import net.hydrotekz.PlexFC.Config;
import net.hydrotekz.PlexFC.Printer;

public class Dashboard {

	public static BorderPane setupDashboard(Button dashboardButton, Button settingsButton, Button scanButton){
		// GridPane
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 0, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);
		// Dropdown
		ChoiceBox<String> autoScan = new ChoiceBox<>();
		HashMap<Long, String> autoScanChoices = new HashMap<Long, String>();
		autoScanChoices.put(5L, "Every 5 minutes");
		autoScanChoices.put(10L, "Every 10 minutes");
		autoScanChoices.put(30L, "Every 30 minutes");
		autoScanChoices.put(0L, "Never");
		for (String choice : autoScanChoices.values()) autoScan.getItems().add(choice);
		autoScan.setValue(autoScanChoices.get(Config.scanInterval));
		autoScan.setOnAction(e -> {
			if (autoScan.getValue().equalsIgnoreCase("Never")) Config.scanInterval = 0;
			else if (autoScan.getValue().equalsIgnoreCase("Every 5 minutes")) Config.scanInterval = 5;
			else if (autoScan.getValue().equalsIgnoreCase("Every 10 minutes")) Config.scanInterval = 10;
			else if (autoScan.getValue().equalsIgnoreCase("Every 30 minutes")) Config.scanInterval = 30;
			Printer.log("Scan interval changed to: " + autoScan.getValue() + " [" + Config.scanInterval + "]");
		});
		// HBox
		HBox topMenu = new HBox();
		topMenu.setPadding(new Insets(10, 10, 0, 10));
		HBox.setMargin(dashboardButton, new Insets(0, 5, 0, 5));
		HBox.setMargin(settingsButton, new Insets(0, 5, 0, 5));
		HBox.setMargin(scanButton, new Insets(0, 5, 0, 5));
		topMenu.getChildren().addAll(dashboardButton, settingsButton, scanButton);
		// Items
		Label label1 = new Label("Movie from path:");
		Label label2 = new Label("Movie to path:");
		Label label3 = new Label("TV show from path:");
		Label label4 = new Label("TV show to path:");
		Label label5 = new Label("Automatically scan:");
		Label label6 = new Label("Starting...");
		label6.textProperty().bind(GUI.currentJob);
		scanButton.getStyleClass().add("button-blue");
		ProgressBar progress = new ProgressBar();
		progress.progressProperty().bind(GUI.progress);
		progress.visibleProperty().bind(GUI.progressVisible);

		GUI.showsOutPath = new TextField(Config.seriesFrom);
		GUI.showsOutPath.setPromptText("where your downloaded episodes are");
		if (Config.seriesFrom != null) GUI.showsOutPath.setText(Config.seriesFrom);
		GUI.showsInPath = new TextField(Config.seriesTo);
		GUI.showsInPath.setPromptText("where you want your TV shows moved");
		if (Config.seriesTo != null) GUI.showsInPath.setText(Config.seriesTo);

		GUI.moviesOutPath = new TextField(Config.moviesFrom);
		GUI.moviesOutPath.setPromptText("where your downloaded movies are");
		if (Config.moviesFrom != null) GUI.moviesOutPath.setText(Config.moviesFrom);
		GUI.moviesInPath = new TextField(Config.moviesTo);
		GUI.moviesInPath.setPromptText("where you want your movies moved");
		if (Config.moviesTo != null) GUI.moviesInPath.setText(Config.moviesTo);
		GUI.moviesInPath.setMaxWidth(400);
		GUI.moviesInPath.setPrefWidth(225);
		// Placements
		GridPane.setConstraints(label1, 0, 3);
		GridPane.setConstraints(GUI.moviesOutPath, 1, 3);
		GridPane.setConstraints(label2, 0, 5);
		GridPane.setConstraints(GUI.moviesInPath, 1, 5);
		GridPane.setConstraints(label3, 0, 7);
		GridPane.setConstraints(GUI.showsOutPath, 1, 7);
		GridPane.setConstraints(label4, 0, 9);
		GridPane.setConstraints(GUI.showsInPath, 1, 9);
		GridPane.setConstraints(label5, 0, 11);
		GridPane.setConstraints(progress, 1, 15);
		GridPane.setConstraints(label6, 1, 16);
		GridPane.setConstraints(autoScan, 1, 11);
		// Return
		grid.getChildren().addAll(label1, label2, label3, label4, label5, progress, label6,
				GUI.showsOutPath, GUI.showsInPath, GUI.moviesOutPath, GUI.moviesInPath, autoScan);
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topMenu);
		borderPane.setLeft(grid);
		return borderPane;
	}

}