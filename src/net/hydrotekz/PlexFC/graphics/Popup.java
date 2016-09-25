package net.hydrotekz.PlexFC.graphics;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class Popup {

	public static void info(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		// Icon
		if (GUI.windowOpen) GUI.window.getIcons().add(new Image("icon.png"));
		alert.setTitle("Information");
		alert.setHeaderText(title);
		alert.setContentText(message);
		if (GUI.windowOpen) GUI.window.setAlwaysOnTop(false);
		alert.showAndWait();
		if (GUI.windowOpen) GUI.window.setAlwaysOnTop(true);
	}

	public static void error(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(title);
		alert.setContentText(message);
		if (GUI.windowOpen) GUI.window.setAlwaysOnTop(false);
		alert.showAndWait();
		if (GUI.windowOpen) GUI.window.setAlwaysOnTop(true);
	}

	public static void warn(String title, String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(title);
		alert.setContentText(message);
		if (GUI.windowOpen) GUI.window.setAlwaysOnTop(false);
		alert.showAndWait();
		if (GUI.windowOpen) GUI.window.setAlwaysOnTop(true);
	}
}