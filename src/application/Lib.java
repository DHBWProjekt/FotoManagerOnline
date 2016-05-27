package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

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
		List<File> listImageFiles = arrayToList(imageFiles);

		return listImageFiles;
	}

	public static List<File> arrayToList(File[] fileArray) {
		List<File> listTemp = new ArrayList<File>();
		for (int i = 0; i < fileArray.length; i++) {
			listTemp.add(fileArray[i]);
		}
		return listTemp;
	}

	public static void fehlerMeldung(Exception e) {
		System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}

	public static void openExplorer(File file) {
		try {
			Desktop.getDesktop().open(file);
		} catch (Exception e) {
			Lib.fehlerMeldung(e);
		}
	}

	public static File openFolder() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Ordnerauswahl");
		return chooser.showDialog(new Stage());
	}

}
