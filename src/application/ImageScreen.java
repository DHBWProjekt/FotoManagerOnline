package application;

import java.io.File;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ImageScreen extends StackPane {

	private ImageView iv1;
	private ImageView iv2;
	private TranslateTransition animationImageFlowOut;
	private TranslateTransition animationImageFlowIn;

	private ImageScreenBuffer isBuffer;

	private int ivInFront = 1; // gibt an, welcher ImageView oben liegt

	private Scene scene;

	public ImageScreen(List<File> listImagesLoad, Scene scene) {
		this.scene = scene;
		isBuffer = new ImageScreenBuffer(listImagesLoad);
		initImageViews();
	}

	private void initImageViews() {
		iv1 = new ImageView();
		iv1.setPreserveRatio(true);
		iv1.fitWidthProperty().bind(scene.widthProperty());
		iv2 = new ImageView();
		iv2.setPreserveRatio(true);
		iv2.fitWidthProperty().bind(scene.widthProperty());
		isBuffer.moveLeft();
		if (isBuffer.getImageAndFileActive() != null) {
			iv1.setImage(isBuffer.getImageAndFileActive().getImage());
			ivInFront = 1;
		}
		this.getChildren().addAll(iv1);
	}

	public void nextImage() {
		if (isBuffer.getBufferSizeRight() > 0 && isBuffer.getImageAndFileActive() != null) {
			isBuffer.moveLeft();
			setImageInBackground();
			animationCenterToLeft();
			animationRightToCenter();
		}
	}

	public void lastImage() {
		if (isBuffer.getBufferSizeRight() > 0 && isBuffer.getImageAndFileActive() != null) {
			isBuffer.moveRight();
			setImageInBackground();
			animationCenterToRight();
			animationLeftToCenter();
		}
	}

	public void up(File folderUp) {
		File fileActualImage = isBuffer.moveUp();
		setImageInBackground();
		animationCenterToTop();
	}

	public void down(File FolderDown) {
		File fileActualImage = isBuffer.moveDown();
		setImageInBackground();
		animationCenterToButtom();
	}

	private void setImageInBackground() {
		if (isBuffer.getImageAndFileActive() != null) {
			if (ivInFront == 1) {
				iv2 = new ImageView(isBuffer.getImageAndFileActive().getImage());
				iv2.setPreserveRatio(true);
				iv2.fitWidthProperty().bind(scene.widthProperty());
				this.getChildren().add(0, iv2);

			} else if (ivInFront == 2) {
				iv1 = new ImageView(isBuffer.getImageAndFileActive().getImage());
				iv1.setPreserveRatio(true);
				iv1.fitWidthProperty().bind(scene.widthProperty());
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
		animationImageFlowOut = new TranslateTransition(new Duration(250.0), getImageViewInFront());
		animationImageFlowOut.setFromX(0);
		animationImageFlowOut.setToX(this.getWidth() * (-1));
		animationImageFlowOut.setInterpolator(Interpolator.LINEAR);
		animationImageFlowOut.setOnFinished(e -> {
			if (this.getChildren().size() > 1) {
				this.getChildren().remove(1);
			}
			changeIvInFront();
		});
		animationImageFlowOut.play();
	}

	private void animationLeftToCenter() {
		animationImageFlowIn = new TranslateTransition(new Duration(250.0), this.getChildren().get(0));
		animationImageFlowIn.setFromX(this.getWidth() * (-1));
		animationImageFlowIn.setToX(0);
		animationImageFlowIn.setInterpolator(Interpolator.LINEAR);
		animationImageFlowIn.play();
	}

	private void animationCenterToRight() {
		animationImageFlowOut = new TranslateTransition(new Duration(250.0), getImageViewInFront());
		animationImageFlowOut.setFromX(0);
		animationImageFlowOut.setToX(this.getWidth());
		animationImageFlowOut.setInterpolator(Interpolator.LINEAR);
		animationImageFlowOut.setOnFinished(e -> {
			if (this.getChildren().size() > 1) {
				this.getChildren().remove(1);
			}
			changeIvInFront();
		});
		animationImageFlowOut.play();
	}

	private void animationRightToCenter() {
		animationImageFlowIn = new TranslateTransition(new Duration(250.0), this.getChildren().get(0));
		animationImageFlowIn.setFromX(this.getWidth());
		animationImageFlowIn.setToX(0);
		animationImageFlowIn.setInterpolator(Interpolator.LINEAR);
		animationImageFlowIn.play();
	}

	private void animationCenterToTop() {
		animationImageFlowOut = new TranslateTransition(new Duration(250.0), getImageViewInFront());
		animationImageFlowOut.setFromY(0);
		animationImageFlowOut.setToY(this.getHeight() * (-1));
		animationImageFlowOut.setInterpolator(Interpolator.LINEAR);
		animationImageFlowOut.setOnFinished(e -> {
			if (this.getChildren().size() > 1) {
				this.getChildren().remove(1);
			}
			changeIvInFront();
		});
		animationImageFlowOut.play();
	}

	private void animationCenterToButtom() {
		this.getChildren().get(0).setLayoutX(0);
		this.getChildren().get(0).setLayoutY(0);
		animationImageFlowOut = new TranslateTransition(new Duration(250.0), getImageViewInFront());
		animationImageFlowOut.setFromY(0);
		animationImageFlowOut.setToY(this.getHeight());
		animationImageFlowOut.setInterpolator(Interpolator.LINEAR);
		animationImageFlowOut.setOnFinished(e -> {
			if (this.getChildren().size() > 1) {
				this.getChildren().remove(1);
			}
			changeIvInFront();
		});
		animationImageFlowOut.play();
	}

	private void changeIvInFront() {
		if (ivInFront == 1) {
			ivInFront = 2;
		} else {
			ivInFront = 1;
		}

	}

}
