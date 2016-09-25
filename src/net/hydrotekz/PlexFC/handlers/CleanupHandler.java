package net.hydrotekz.PlexFC.handlers;

import java.io.File;

import org.apache.commons.io.FileUtils;

import net.hydrotekz.PlexFC.Printer;
import net.hydrotekz.PlexFC.Utils;

public class CleanupHandler {
	
	public static int performCleanup(File fromFile){
		int i = 0;
		for (File file : ToDoHandler.toDelete){
			file = Utils.refresh(file);
			if (file.exists() && !file.getName().equalsIgnoreCase(fromFile.getName()))
				try {
					FileUtils.forceDelete(file);
				} catch (Exception e) {
					Printer.log("Failed to delete file: " + file.getAbsolutePath());
				}
			i++;
		}
		return i;
	}

	public static int deleteUnwantedFiles(File file){
		int i = 0;
		try {
			if (file.isFile() && !file.isDirectory()){
				String name = file.getName();
				if (name.contains(".")){
					String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
					if (ext.endsWith("ignore") || ext.contains("random") || ext.endsWith("txt") || ext.endsWith("rar")){
						FileUtils.forceDelete(file);
						i++;
					}
				} else {
					FileUtils.forceDelete(file);
					i++;
				}

			} else if (file.isDirectory() && !file.isHidden()){
				for (File f : file.listFiles()){
					deleteUnwantedFiles(Utils.refresh(f));
				}
			}
		} catch (Exception ex){
			Printer.log("Deleting of unwanted files loop aborted due the error below.");
			Printer.log(ex);
		}
		return i;
	}
}