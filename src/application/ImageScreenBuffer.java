package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageScreenBuffer {

	private List<File> listFileImagesRight;
	private List<File> listFileImagesLeft;

	// Buffer für Images der kommenden 4 Bilder
	private List<ImageAndFile> listImageAndFileRight;

	// Buffer für Images der letzte 4 bereits angezeigten Bilder
	private List<ImageAndFile> listImageAndFileLeft;

	private ImageAndFile imageAndFileActive = null;

	public ImageScreenBuffer(List<File> listImagesLoad) {
		this.listFileImagesRight = listImagesLoad;
		this.listFileImagesLeft = new ArrayList<File>();
		initListImageAndFileRight();
		initListImageAndFileLeft();
	}

	private void initListImageAndFileRight() {
		listImageAndFileRight = new ArrayList<ImageAndFile>();
		int imageBufferSize = 4;
		if (imageBufferSize > listFileImagesRight.size()) {
			imageBufferSize = listFileImagesRight.size();
		}

		for (int i = 0; i < imageBufferSize; i++)
			listImageAndFileRight.add(new ImageAndFile(listFileImagesRight.remove(0)));
	}

	private void initListImageAndFileLeft() {
		listImageAndFileLeft = new ArrayList<ImageAndFile>();
	}

	public void moveLeft() {
		if (imageAndFileActive != null) {
			listImageAndFileLeft.add(0, imageAndFileActive);
			imageAndFileActive = null;
			checkBufferSizeLeft();
		}
		imageAndFileActive = null;
		System.out.println("MoveLeft: SizeRight: " + listImageAndFileRight.size());
		if (listImageAndFileRight.size() > 0) {
			imageAndFileActive = listImageAndFileRight.remove(0);
			if (listFileImagesRight.size() > 0) {
				listImageAndFileRight.add(new ImageAndFile(listFileImagesRight.remove(0)));
			}
		}
	}

	public void moveRight() {
		if (imageAndFileActive != null) {
			listImageAndFileRight.add(0, imageAndFileActive);
			imageAndFileActive = null;
			checkBufferSizeRight();
		}
		System.out.println("MoveRight: SizeLeft: " + listImageAndFileLeft.size());
		imageAndFileActive = null;
		if (listImageAndFileLeft.size() > 0) {
			imageAndFileActive = listImageAndFileLeft.remove(0);
			if (listFileImagesLeft.size() > 0) {
				listImageAndFileLeft.add(new ImageAndFile(listFileImagesLeft.remove(0)));
			}
		}
	}

	public File moveUp() {
		return moveUpOrDown();
	}

	public File moveDown() {
		return moveUpOrDown();
	}

	private File moveUpOrDown() {
		File fileReturn = null;
		if (imageAndFileActive != null) {
			fileReturn = imageAndFileActive.getFileImage();
			imageAndFileActive = null;
		}
		if (listImageAndFileRight.size() > 0) {
			imageAndFileActive = listImageAndFileRight.remove(0);
			if (listFileImagesRight.size() > 0) {
				listImageAndFileRight.add(new ImageAndFile(listFileImagesRight.remove(0)));
			}
		}
		return fileReturn;
	}

	public void checkBufferSizeLeft() {
		if (listImageAndFileLeft.size() > 4) {
			listFileImagesLeft.add(0, listImageAndFileLeft.remove(4).getFileImage());
		}
	}

	public void checkBufferSizeRight() {
		if (listImageAndFileRight.size() > 4) {
			listFileImagesRight.add(0, listImageAndFileRight.remove(4).getFileImage());
		}
	}

	public ImageAndFile getImageAndFileActive() {
		return imageAndFileActive;
	}
}
