package application;

import java.io.File;

import javafx.scene.image.Image;

public class ImageAndFile {
	private Image image;
	private File fileImage;

	public ImageAndFile(File imageFile) {
		image = new Image(imageFile.toURI().toString());
		fileImage = imageFile;
	}

	public Image getImage() {
		return image;
	}

	public File getFileImage() {
		return fileImage;
	}
}
