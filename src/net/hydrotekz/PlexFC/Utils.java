package net.hydrotekz.PlexFC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Utils {

	private static int getYearPlain(String s){
		if (s.length() == 4){
			try {
				int yr = Integer.parseInt(s);
				if (yr >= 1895 && yr <= 2018){
					return yr;
				} else return 0;
			} catch (Exception e){
				return 0;
			}
		} else return 0;
	}

	public static boolean isApproved(String s){
		if (s != null){
			File f = new File(s);
			if (f.exists()) return true;
		}
		return false;
	}

	public static boolean isVideo(File f){
		String[] l = Config.videoExtensions.split(",");
		for (String s : l){
			if (f.getName().endsWith("." + s)) return true;
		}
		return false;
	}

	public static File refresh(File f){
		return new File(f.getAbsolutePath());
	}

	public static int getYear(String s){
		if (s.length() == 4 || s.length() == 6){
			String yr = "";
			for (String c : s.split("")){
				if (!c.contains("(") && !c.contains(")")) yr+=c;
			}
			if (yr.length() == 4){
				int year = getYearPlain(yr);
				return year;
			}
		}
		return 0;
	}

	public static int getYear(String[] split){
		for (String s : split){
			int year = getYear(s);
			if (year > 0) return year;
		}
		return 0;
	}

	public static String removeChar(String string, String c){
		StringBuilder sb = new StringBuilder();
		for (String s : string.split("")){
			if (!s.equalsIgnoreCase(c)) sb.append(s);
		}
		return sb.toString();
	}

	public static String replaceChar(String string, String from, String to){
		StringBuilder sb = new StringBuilder();
		for (String s : string.split("")){
			if (!s.equalsIgnoreCase(from)) sb.append(s);
			else sb.append(to);
		}
		return sb.toString();
	}

	public static boolean isLocked(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			if (in!=null) in.close();
			return false;
		} catch (FileNotFoundException e) {
			return true;
		} catch (Exception e) {
			Printer.log("Failed to check if file is locked or not.");
			Printer.log(e);
		}
		return false;
	}
}