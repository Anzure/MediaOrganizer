package net.hydrotekz.PlexFC.graphics;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.hydrotekz.PlexFC.PlexFC;
import net.hydrotekz.PlexFC.handlers.ScanHandler;

public class MainWindow {

	public static void startMainWindow(Stage primaryWindow){
		GUI.window = primaryWindow;
		// Icon
		GUI.window.getIcons().add(new Image("icon.png"));
		// Buttons
		GUI.dashboardButton1 = new Button("Dashboard");
		GUI.dashboardButton1.setOnAction(e -> {
			GUI.window.setScene(GUI.dashboard);
			GUI.window.setTitle("PlexFC v" + PlexFC.version + " - Dashboard");
		});

		GUI.dashboardButton2 = new Button("Dashboard");
		GUI.dashboardButton2.setOnAction(e -> {
			GUI.window.setScene(GUI.dashboard);
			GUI.window.setTitle("PlexFC v" + PlexFC.version + " - Dashboard");
		});

		GUI.settingsButton1 = new Button("Advanced settings");
		GUI.settingsButton1.setOnAction(e -> {
			GUI.window.setScene(GUI.settings);
			GUI.window.setTitle("PlexFC v" + PlexFC.version + " - Advanced settings");
		});

		GUI.settingsButton2 = new Button("Advanced settings");
		GUI.settingsButton2.setOnAction(e -> {
			GUI.window.setScene(GUI.settings);
			GUI.window.setTitle("PlexFC v" + PlexFC.version + " - Advanced settings");
		});

		GUI.scanButton = new Button("Manual scan");
		GUI.scanButton.setOnAction(e -> {
			ScanHandler.scan(true);
		});
		// Dashboard
		GUI.dashboard = new Scene(Dashboard.setupDashboard(GUI.dashboardButton1, GUI.settingsButton1, GUI.scanButton), 450, 350);

		// Settings
		GUI.settings = new Scene(AdvancedSettings.setupSettings(GUI.dashboardButton2, GUI.settingsButton2), 450, 350);

		// Add CSS and finish
		GUI.dashboard.getStylesheets().add("Style.css");
		GUI.settings.getStylesheets().add("Style.css");
		GUI.window.setScene(GUI.dashboard);
		GUI.window.setTitle("PlexFC v" + PlexFC.version + " - Dashboard");
		GUI.window.setOnCloseRequest(e -> {
			e.consume();
			GUI.window.hide();
			System.exit(0);
		});
		GUI.window.setResizable(false);
		GUI.window.setAlwaysOnTop(true);
		GUI.window.show();
		GUI.windowOpen = true;
		
		// Enable auto saver
		GUI.autoSaver();
	}

}