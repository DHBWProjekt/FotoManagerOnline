package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private File pathUp = null;
	private File pathDown = null;

	private Label labelPathUp = new Label("\uf07c");
	private Label labelPathDown = new Label("\uf07c");
	private Label labelCenter = new Label("\uf03e");

	private List<File> listImages = new ArrayList<File>();

	private int pictureCounter = 0;

	private ImageView activeImageView;

	private int counter = 0;

	private boolean isVisibleLabelPath = true;

	private BorderPane topBorderPane;
	private BorderPane bottomBorderPane;
	private BorderPane centerBorderPane;

	private TranslateTransition animationImageView;
	private FadeTransition animationTopBorderPane;
	private FadeTransition animationBottomBorderPane;

	private StackPane root;
	private Scene scene;

	public void start(Stage primaryStage) {
		try {
			Font.loadFont(getClass().getResource("resources/fontawesome-webfont.ttf").toExternalForm(), 20);
			root = new StackPane();
			BorderPane borderPane = new BorderPane();
			this.initAnchorPaneDragAndDrop(borderPane);

			topBorderPane = new BorderPane();
			topBorderPane.setCenter(labelPathUp);

			topBorderPane.setOnMouseReleased(e -> {
				if (isVisibleLabelPath == true) {
					pathUp = Lib.openFolder();
					if (pathUp != null) {
						labelPathUp.setText(pathUp.getName());
					} else {
						labelPathUp.setText("\uf07c");
					}
				}
			});

			bottomBorderPane = new BorderPane();
			bottomBorderPane.setCenter(labelPathDown);

			bottomBorderPane.setOnMouseReleased(e -> {
				if (isVisibleLabelPath == true) {
					pathDown = Lib.openFolder();
					if (pathDown != null) {
						labelPathDown.setText(pathDown.getName());
					} else {
						labelPathUp.setText("\uf07c");
					}
				}
			});

			borderPane.setTop(topBorderPane);
			borderPane.setBottom(bottomBorderPane);

			labelCenter.getStyleClass().add("lblCenter");
			centerBorderPane = new BorderPane(labelCenter);
			centerBorderPane.getStyleClass().add("centerBP");
			borderPane.setCenter(centerBorderPane);

			labelPathUp.getStyleClass().add("lblPath");
			labelPathDown.getStyleClass().add("lblPath");

			activeImageView = createImageView(borderPane.widthProperty());
			// centerAnchorPane.getChildren().add(activeImageView);
			root.getChildren().add(activeImageView);
			root.getChildren().add(borderPane);

			animationImageView = new TranslateTransition(new Duration(500.0), activeImageView);
			animationImageView.setFromX(root.getWidth());
			animationImageView.setToX(0);
			animationImageView.setInterpolator(Interpolator.LINEAR);

			animationBottomBorderPane = new FadeTransition(Duration.millis(250), bottomBorderPane);
			animationBottomBorderPane.setFromValue(1.0);
			animationBottomBorderPane.setToValue(0);
			animationBottomBorderPane.setInterpolator(Interpolator.LINEAR);

			animationTopBorderPane = new FadeTransition(Duration.millis(250), topBorderPane);
			animationTopBorderPane.setFromValue(1.0);
			animationTopBorderPane.setToValue(0);
			animationTopBorderPane.setInterpolator(Interpolator.LINEAR);

			animationBottomBorderPane.setOnFinished(e -> {
				if (isVisibleLabelPath == true) {
					animationTopBorderPane.setFromValue(0);
					animationTopBorderPane.setToValue(1.0);
					animationBottomBorderPane.setFromValue(0);
					animationBottomBorderPane.setToValue(1.0);
					isVisibleLabelPath = false;
				} else {
					animationTopBorderPane.setFromValue(1.0);
					animationTopBorderPane.setToValue(0);
					animationBottomBorderPane.setFromValue(1.0);
					animationBottomBorderPane.setToValue(0);
					isVisibleLabelPath = true;
				}

			});

			borderPane.setOnMouseClicked(e -> {
				animationBottomBorderPane.play();
				animationTopBorderPane.play();
				System.out.println("Mouse Clicked");
			});

			scene = new Scene(root, 600, 600);
			scene.setOnKeyReleased(e -> handleKeyEvent(e));
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Initializing of DragAndDrop on the given Node
	 * 
	 * @param Node
	 *            - node that should be DragAndDrop-able
	 */
	public void initAnchorPaneDragAndDrop(Node node) {

		node.setOnDragOver(event -> {
			Dragboard dragBoard = event.getDragboard();
			if (dragBoard.hasFiles()) {
				event.acceptTransferModes(TransferMode.ANY);
			}
			// event.consume();
		});

		node.setOnDragDropped(event -> {
			Dragboard dragBoard = event.getDragboard();

			setImageFolder(null);

			if (dragBoard.hasFiles()) {
				if (dragBoard.getFiles().size() == 1) {
					if (dragBoard.getFiles().get(0).isDirectory()) {
						for (File file : dragBoard.getFiles()) {
							System.out.println(file.isDirectory());
							setImageFolder(Lib.readImagesFromDirectory(dragBoard.getFiles().get(0)));
						}
					} else if (dragBoard.getFiles().get(0).isFile()) {
						setImageFolder(dragBoard.getFiles());
					}
				} else {
					setImageFolder(dragBoard.getFiles());
				}

				System.out.println("List of files incoming");
			}
			if (getImageFolder() != null) {
				/*
				 * TODO first picture should be shown
				 */

				setPictureToPane(listImages.get(counter));

				System.out.println("Erstes Bild wird angezeigt");
			}

			event.setDropCompleted(getImageFolder() != null);
			event.consume();

		});

	}

	/**
	 * Sets the picture on the imageView
	 * 
	 * @param pictureFile
	 *            - picture that shall be shown
	 */
	public void setPictureToPane(File pictureFile) {

		// topBorderPane.setVisible(false);
		// bottomBorderPane.setVisible(false);
		centerBorderPane.setVisible(false);

		Image myImage = new Image(pictureFile.toURI().toString());
		activeImageView.setImage(myImage);
		animationImageView.playFromStart();

	}

	/**
	 * Creation of the ImageView, which is binded to the widthProperty
	 * 
	 * @param widthProperty
	 * @return ImageView
	 */

	private ImageView createImageView(ReadOnlyDoubleProperty widthProperty) {
		// maintain aspect ratio
		ImageView imageView = new ImageView();
		// set aspect ratio
		imageView.setPreserveRatio(true);
		// resize based on the scene
		imageView.fitWidthProperty().bind(widthProperty);
		return imageView;
	}

	/**
	 * Initializing of the handleKeyEvent method of the setOnKeyReleased method
	 */

	private void handleKeyEvent(KeyEvent e) {

		System.out.println(e.getCode());
		System.out.println(counter);
		System.out.println("Die Größe der Liste ist " + listImages.size());
		System.out.println("Der PictureCounter ist: " + pictureCounter);

		if (pictureCounter > 0) {
			if (e.getCode().equals(KeyCode.UP) || e.getCode().equals(KeyCode.DOWN)) {

				boolean worked = nextPicture();

				int counter2 = counter;
				if (worked == true) {
					counter2--;
				}
				System.out.println(listImages.get(counter2).toPath());
				File file = null;
				if (e.getCode().equals(KeyCode.UP)) {
					System.out.println(pathUp.toPath());
					if (pathDown != null) {
						file = new File(pathUp.toPath() + "/" + listImages.get(counter2).getName());
					}
				} else if (e.getCode().equals(KeyCode.DOWN)) {
					System.out.println(pathDown.toPath());
					if (pathDown != null) {
						file = new File(pathDown.toPath() + "/" + listImages.get(counter2).getName());
					}
				}
				try {
					if (file != null) {
						Files.move(listImages.get(counter2).toPath(), file.toPath());
					}
				} catch (AccessDeniedException e2) {
					e2.printStackTrace();
					// TODO Meldung auf Oberfläche ausgeben
					System.out.println("Pfad existiert nicht oder wurde noch nicht ausgewählt");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				pictureCounter--;

			} else if (e.getCode().equals(KeyCode.RIGHT)) {
				nextPicture();
			} else if (e.getCode().equals(KeyCode.LEFT)) {
				lastPicture();

			}
		}
		if (pictureCounter == 0) {
			File finishedFile = new File("./img/fertig.png");
			System.out.println("Das File exisitert: " + finishedFile.exists());
			System.out.println("Der Pfad ist : " + finishedFile.getAbsolutePath());
			Image myImage = new Image(finishedFile.toURI().toString());
			activeImageView.setImage(myImage);
		}

	}

	/**
	 * Sets the counter one step forward, if the counter is smaller than the
	 * size of the Image-List
	 * 
	 * @return boolean - if the stepping forward was successful
	 */

	private boolean nextPicture() {
		if (counter < listImages.size() - 1) {
			counter++;
			animationImageView.setFromX(root.getWidth());
			setPictureToPane(listImages.get(counter));
			return true;
		}
		return false;
	}

	/**
	 * Sets the counter one step back, if the counter is bigger than Zero.
	 */
	private void lastPicture() {
		if (counter > 0) {
			counter--;
			animationImageView.setFromX(root.getWidth() * (-1));
			setPictureToPane(listImages.get(counter));
		}
	}

	private void mooveFile() {
		/*
		 * TODO
		 */
	}

	/**
	 * Increases the pictureCounter every time new pictures are added
	 * 
	 * @param picturesInList
	 *            - number of pictures that are in the list that is loaded
	 */
	private void increasePictureCounter(int picturesInList) {
		System.out.println("Ich erhöhe den PictureCounter");
		pictureCounter = pictureCounter + picturesInList;
	}

	public List<File> getImageFolder() {
		return listImages;
	}

	public void setImageFolder(List<File> imageFolder) {
		if (imageFolder != null) {
			increasePictureCounter(imageFolder.size());
		} else {
			increasePictureCounter(0);
		}
		this.listImages = imageFolder;
	}
}
