package application;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

public abstract class Lib {

	public static List<File> readImagesFromDirectory(File directory) {
		File[] imageFiles = directory.listFiles();

		FileFilter ff = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.toURI().toString().toLowerCase().endsWith(".jpg") == true) {
					return true;
				} else if (pathname.toURI().toString().toLowerCase().endsWith(".png") == true) {
					return true;
				} else if (pathname.toURI().toString().toLowerCase().endsWith(".dvi") == true) {
					return true;
				}
				return false;
			}
		};
		imageFiles = directory.listFiles(ff);
		System.out.println("Anzahl Files: " + imageFiles.length);
		List<File> listImageFiles = Arrays.asList(imageFiles);

		return listImageFiles;
	}
}
