package net.hydrotekz.PlexFC.scanners;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.text.WordUtils;

import net.hydrotekz.PlexFC.Config;
import net.hydrotekz.PlexFC.Printer;
import net.hydrotekz.PlexFC.Utils;
import net.hydrotekz.PlexFC.handlers.ToDoHandler;

public class MoviesScanner {

	public static boolean scanFile(File fromFile){
		// Scanning file
		Printer.log("");
		String file = FilenameUtils.getBaseName(FilenameUtils.removeExtension(fromFile.getName()))/*.replaceAll("\\.", " ").replaceAll("_", " ")*/;
		file = Utils.replaceChar(file, ".", " ");
		file = Utils.replaceChar(file, "_", " ");
		file = Utils.replaceChar(file, "[", "");
		file = Utils.replaceChar(file, "]", "");
		Printer.log("Scanning \"" + file + "\"...");
		String[] split = file.split(" ");
		String movie = "";
		int i = 0;
		int year = Utils.getYear(split);
		//		if (!file.contains("720") && !file.contains("1080") && !file.contains("WEB-DL") && !file.contains("BluRay") && year <= 0){
		//			return false;
		//		}
		for (String s : split){
			if (s != null && s.length() > 0){
				// Check if episode
				if (s.toUpperCase().startsWith("S") && s.toUpperCase().contains("E")){
					String[] array = s.toUpperCase().replaceFirst("S", "").split("E");
					try {
						if (array.length == 2){
							int episode = Integer.parseInt(array[1]);
							int season = Integer.parseInt(array[0]);
							if (episode >= 1 && episode <= 50 && season >= 0 && season <= 50){
								Printer.log("The video file seems to be a episode instead of a movie.");
								return false;
							}
						} else Printer.log("" + array.length);
					} catch (Exception e){}
				}
				if (s.toUpperCase().contains("X")){
					String[] array = s.toUpperCase().split("X");
					try {
						if (array.length == 2){
							int episode = Integer.parseInt(array[1]);
							int season = Integer.parseInt(array[0]);
							if (episode >= 1 && episode <= 50 && season >= 0 && season <= 50){
								Printer.log("The video file seems to be a episode instead of a movie.");
								return false;
							}
						}
					} catch (Exception e){}
				}
				if (s.toLowerCase().startsWith("ep")){
					String ep = s.toLowerCase().replaceFirst("ep", "");
					try {
						int episode = Integer.parseInt(ep);
						if (episode >= 1 && episode <= 50){
							Printer.log("The video file seems to be a episode instead of a movie.");
							return false;
						}
					} catch (Exception ex){
						if (ep.equalsIgnoreCase("Ep")){
							Printer.log("The video file seems to be a episode instead of a movie.");
							return false;
						}
					}
				}
				if (s.toLowerCase().startsWith("episode")){
					String ep = s.toLowerCase().replaceFirst("episode", "");
					try {
						int episode = Integer.parseInt(ep);
						if (episode >= 1 && episode <= 50){
							Printer.log("The video file seems to be a episode instead of a movie.");
							return false;
						}
					} catch (Exception ex){
						if (ep.equalsIgnoreCase("episode")){
							Printer.log("The video file seems to be a episode instead of a movie.");
							return false;
						}
					}
				}
				// Check year
				if (i > 0){
					int yr = Utils.getYear(s);
					if (yr > 0){
						break;
					}
				}
				// Check quality
				String quality = Utils.removeChar(s, "p");
				if (quality.equals("720") || quality.equals("1080") || quality.equalsIgnoreCase("WEB-DL") || quality.equalsIgnoreCase("BluRay")){
					break;
				}
				// Build movie name
				if ((s.equals("of") || s.equals("the")) && i > 0) movie+=" " + s;
				else if (i>0) movie+=" " + WordUtils.capitalize(s);
				else movie+=WordUtils.capitalize(s);
				i++;
			}
		}

		// Moving file
		if (movie != null && movie != "" && movie.length() >= 2){
			// Fix weird ending
			if (movie.contains(", The")){
				movie = movie.replaceFirst(", The", "");
				movie = "The " + movie;
			}
			if (year > 0) movie+=" (" + year + ")";
			try {
				String ext = fromFile.getName().substring(fromFile.getName().lastIndexOf(".") + 1);
				File toFile = new File(Config.moviesTo + File.separator + "" + movie + File.separator + movie + "." + ext);
				toFile.getParentFile().mkdirs();
				long fromSize = fromFile.length();
				if (fromSize < Config.minMovieSize*1024*1024){
					Printer.log("The video file is too small to be a movie.");
					return false;
				}
				Printer.log("The file was identified as \"" + movie + "\"!");
				if (toFile.exists()){
					if (fromSize > toFile.length()){
						if (!Utils.isLocked(toFile)) FileUtils.forceDelete(toFile);
						if (!toFile.exists()) Printer.log("Existing file was found at destination, and will be overwritten with a lager file.");
					} else {
						FileUtils.forceDelete(fromFile);
						if (Config.doCleanup){
							File parent = fromFile.getParentFile();
							if (!parent.getAbsolutePath().equalsIgnoreCase(new File(Config.moviesFrom).getAbsolutePath())) ToDoHandler.toDelete.add(parent);
						}
						Printer.log("Existing file was found at destination, and cancelled due destination file is larger.");
						return false;
					}
				}
				try {
					File folder = toFile.getParentFile();
					for (File checkFile : folder.listFiles()){
						if (checkFile.isFile()){
							String fileName = FilenameUtils.removeExtension(checkFile.getName());
							if (fileName.equalsIgnoreCase(FilenameUtils.removeExtension(toFile.getName()))){
								if (fromSize > checkFile.length()){
									FileUtils.forceDelete(checkFile);
									Printer.log("Duplicate found with a smaller file size, it was therefor deleted.");
								} else {
									FileUtils.forceDelete(fromFile);
									if (Config.doCleanup){
										File parent = fromFile.getParentFile();
										if (!parent.getAbsolutePath().equalsIgnoreCase(new File(Config.moviesFrom).getAbsolutePath())) ToDoHandler.toDelete.add(parent);
									}
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
				if (!toFile.exists()){
					Printer.log("File is scheduled to be moved.");
					ToDoHandler.toMove.put(fromFile, toFile);
					if (Config.doCleanup){
						File folder = fromFile.getParentFile();
						if (!folder.getAbsolutePath().equalsIgnoreCase(new File(Config.moviesFrom).getAbsolutePath())) ToDoHandler.toDelete.add(folder);
					}
				} else {
					Printer.log("Unable to delete existing file!");
				}
			} catch (Exception e) {
				Printer.log("Failed to move file!");
				Printer.log(e);
				System.exit(0);
			}
		} else {
			Printer.log("Failed to idenitfy movie based on the file name, sorry!");
		}
		return false;
	}
}