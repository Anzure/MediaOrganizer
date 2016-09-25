package net.hydrotekz.PlexFC.handlers;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import net.hydrotekz.PlexFC.Printer;

public class DupeHandler {
	
	public static boolean checkForDupe1(File fromFile, File toFile){
		try {
			if (toFile.exists()){
				if (fromFile.length() > toFile.length()){
					FileUtils.forceDelete(toFile);
					Printer.log("Duplicate found with a smaller file size, it was therefor deleted.");
				} else {
					FileUtils.forceDelete(fromFile);
					Printer.log("Duplicate found with a larger file size, so this one was terminated.");
					return false;
				}
			}
			
		} catch (Exception ex){
			Printer.log("Failed to check for duplicates with other file types.");
			Printer.log(ex);
		}
		return true;
	}

	public static boolean checkForDupe2(File fromFile, File toFile){
		try {
			File folder = toFile.getParentFile();
			for (File checkFile : folder.listFiles()){
				if (checkFile.isFile()){
					String fileName = FilenameUtils.removeExtension(checkFile.getName());
					if (fileName.equalsIgnoreCase(FilenameUtils.removeExtension(toFile.getName()))){
						if (fromFile.length() > checkFile.length()){
							FileUtils.forceDelete(checkFile);
							Printer.log("Duplicate found with a smaller file size, it was therefor deleted.");
						} else {
							FileUtils.forceDelete(fromFile);
							Printer.log("Duplicate found with a larger file size, so this one was terminated.");
							return false;
						}
					}
				}
			}
			
		} catch (Exception ex){
			Printer.log("Failed to check for duplicates with other file types.");
			Printer.log(ex);
		}
		return true;
	}

}