package application;

import java.io.File;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ImageScreen extends StackPane {

	private ImageView iv1;
	private ImageView iv2;
	private TranslateTransition animationImageView;

	private ImageScreenBuffer isBuffer;

	private int ivInFront = 1; // gibt an, welcher ImageView oben liegt

	public ImageScreen(List<File> listImagesLoad) {
		isBuffer = new ImageScreenBuffer(listImagesLoad);
		initImageViews();
	}

	private void initImageViews() {
		isBuffer.moveLeft();
		if (isBuffer.getImageAndFileActive() != null) {
			iv1 = new ImageView(isBuffer.getImageAndFileActive().getImage());
			ivInFront = 1;
		}
		this.getChildren().add(iv1);
	}

	private void nextImage() {
		isBuffer.moveLeft();
		setImageInBackground();
		animationCenterToLeft();
	}

	private void lastImage() {
		isBuffer.moveRight();
		setImageInBackground();
		animationCenterToRight();
	}

	private void setImageInBackground() {
		if (isBuffer.getImageAndFileActive() != null) {
			if (ivInFront == 1) {
				iv2 = new ImageView(isBuffer.getImageAndFileActive().getImage());
				this.getChildren().add(0, iv2);
			} else if (ivInFront == 2) {
				iv1 = new ImageView(isBuffer.getImageAndFileActive().getImage());
				this.getChildren().add(0, iv1);
			}
		}
	}

	private ImageView getImageViewInFront() {
		if (ivInFront == 1) {
			return iv1;
		} else {
			return iv2;
		}
	}

	private void animationCenterToLeft() {
		animationImageView = new TranslateTransition(new Duration(500.0), getImageViewInFront());
		animationImageView.setFromX(0);
		animationImageView.setToX(this.getWidth() * (-1));
		animationImageView.setInterpolator(Interpolator.LINEAR);
		animationImageView.play();
	}

	private void animationCenterToRight() {
		animationImageView = new TranslateTransition(new Duration(500.0), getImageViewInFront());
		animationImageView.setFromX(0);
		animationImageView.setToX(this.getWidth());
		animationImageView.setInterpolator(Interpolator.LINEAR);
		animationImageView.play();
	}

	private void animationCenterToTop() {
		animationImageView = new TranslateTransition(new Duration(500.0), getImageViewInFront());
		animationImageView.setFromY(0);
		animationImageView.setToY(this.getHeight() * (-1));
		animationImageView.setInterpolator(Interpolator.LINEAR);
		animationImageView.play();
	}

	private void animationCenterToButtom() {
		animationImageView = new TranslateTransition(new Duration(500.0), getImageViewInFront());
		animationImageView.setFromY(0);
		animationImageView.setToY(this.getHeight());
		animationImageView.setInterpolator(Interpolator.LINEAR);
		animationImageView.play();

	}

}
