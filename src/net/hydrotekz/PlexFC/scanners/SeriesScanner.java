package net.hydrotekz.PlexFC.scanners;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.text.WordUtils;

import net.hydrotekz.PlexFC.Config;
import net.hydrotekz.PlexFC.Printer;
import net.hydrotekz.PlexFC.Utils;
import net.hydrotekz.PlexFC.handlers.ToDoHandler;

public class SeriesScanner {

	public static boolean scanFile(File fromFile){
		// Scanning file
		Printer.log("");
		String file = FilenameUtils.getBaseName(FilenameUtils.removeExtension(fromFile.getName())).replaceAll("\\.", " ").replaceAll("_", " ").replaceFirst(" - ", " ");
		Printer.log("Scanning \"" + file + "\"...");
		String[] split = file.split(" ");
		String serie = WordUtils.capitalize(split[0]);
		String season = null;
		String episode = null;
		int year = 0;
		int i = 0;
		for (String s : split){
			if (i > 0 && year == 0){
				year = Utils.getYear(s);
			}
			if (season == null && episode == null && s.length() >= 4 && s.length() <= 9){
				if (s.toUpperCase().startsWith("S") && s.toUpperCase().contains("E")){
					String[] array = s.toUpperCase().replaceFirst("S", "").split("E");
					if (array.length == 2 || array.length == 3){
						try {
							season = "s" + String.format("%02d", Integer.parseInt(array[0]));
							String[] toFrom = null;
							boolean bool = true;
							if (array[1].contains("-")){
								bool = false;
								toFrom = array[1].split("-");
								if (toFrom.length == 2){
									int from = Integer.parseInt(toFrom[0]);
									int to = Integer.parseInt(toFrom[1]);
									boolean isFirst = true;
									for (int ep = from ; ep < to; ep++){
										if (isFirst) episode = "e" + String.format("%02d", ep);
										else {
											episode += "e" + String.format("%02d", ep);
											isFirst = false;
										}
									}
								}
							}
							if (bool) episode = "e" + String.format("%02d", Integer.parseInt(array[1]));
							if (array.length == 3) episode += "e" + String.format("%02d", Integer.parseInt(array[2]));

						} catch (Exception e){}
					}
				} else if (s.contains("x")){
					String[] array = s.split("x");
					if (array.length == 2){
						try {
							season = "s" + String.format("%02d", Integer.parseInt(array[0]));
							episode = "e" + String.format("%02d", Integer.parseInt(array[1]));

						} catch (Exception e){}
					}
				}
			}
			if (season == null && episode == null && year == 0 && i > 0){
				if (s.equals("of") || s.equals("the")) serie+=" " + s;
				else serie+=" " + WordUtils.capitalize(s);
			}
			i++;
		}
		if (serie.replaceAll("-", "").replaceAll(" ", "").equalsIgnoreCase("Tvd")) serie="The Vampire Diaries";
		if (serie.equalsIgnoreCase("Doctor Who")) year=2005;
		if (year != 0) serie+=" (" + year + ")";
		if (serie.equals("The Office US")){ // TODO: ReplaceLast instead of ReplaceFirst
			if (serie.endsWith(" US")) serie = serie.replaceFirst(" US", " (US)");
			else if (serie.endsWith(" UK")) serie = serie.replaceFirst(" UK", " (UK)");
		}

		// Moving file
		if (season != null && episode != null && !serie.equalsIgnoreCase(season + episode) && !serie.toLowerCase().contains((season + episode).toLowerCase())){
			String newFileName = serie + " - " + season + episode;
			try {
				String ext = fromFile.getName().substring(fromFile.getName().lastIndexOf(".") + 1);;
				File toFile = new File(Config.seriesTo + File.separator + "" + serie + File.separator + "Season " + season.replaceFirst("s", "") + File.separator
						+ newFileName + "." + ext);
				toFile.getParentFile().mkdirs();
				long fromSize = fromFile.length();
				if (fromSize < Config.minEpisodeSize*1024*1024){
					Printer.log("The video file is too small to be an episode.");
					return false;
				}
				Printer.log("The file was identified as \"" + newFileName + "\"!");
				if (toFile.exists()){
					if (fromSize > toFile.length()){
						FileUtils.forceDelete(toFile);
						if (!toFile.exists()) Printer.log("Existing file was found at destination, and will be overwritten with a lager file.");
					} else {
						Printer.log("Existing file was found at destination, and cancelled due destination file is a lager video file.");
						return false;
					}
				}
				if (!toFile.exists()){
					ToDoHandler.toMove.put(fromFile, toFile);
					Printer.log("File is scheduled to be moved.");
				} else {
					Printer.log("Unable to delete existing file!");
				}

			} catch (Exception e) {
				Printer.log("Failed to move file!");
				Printer.log(e);
				System.exit(0);
			}
		} else {
			Printer.log("Failed to idenitfy episode based on the file name, sorry!");
		}
		return false;
	}
}