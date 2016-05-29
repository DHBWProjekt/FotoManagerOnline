package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import application.panes.PaneFinished;
import application.panes.PaneFolderPath;
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

	private List<File> listImagesLoad;
	private List<File> listImagesLeft = new ArrayList<File>();
	private File fileImageActiv;

	private ImageView activeImageView;
	private Image myImage;

	private boolean isVisibleLabelPath = true;

	private BorderPane borderPane;

	private BorderPane topBorderPane;
	private BorderPane bottomBorderPane;
	private BorderPane centerBorderPane;

	private PaneFolderPath topPaneFolderPath;
	private PaneFolderPath bottomPaneFolderPath;

	private TranslateTransition animationImageView;

	private FadeTransition animationTopBorderPane;
	private FadeTransition animationBottomBorderPane;

	private StackPane root;
	private Scene scene;

	public void start(Stage primaryStage) {
		try {
			Font.loadFont(getClass().getResource("resources/fontawesome-webfont.ttf").toExternalForm(), 20);
			root = new StackPane();
			borderPane = new BorderPane();
			this.initAnchorPaneDragAndDrop(borderPane);

			topBorderPane = new BorderPane();
			topBorderPane.setCenter(labelPathUp);

			labelPathUp.setOnMouseReleased(e -> {
				if (isVisibleLabelPath == true) {
					pathUp = Lib.openFolder();
					if (pathUp != null) {
						topPaneFolderPath = new PaneFolderPath(pathUp);
						topBorderPane.setCenter(topPaneFolderPath);
					} else {
						topBorderPane.setCenter(labelPathUp);
					}
				}
			});

			bottomBorderPane = new BorderPane();
			bottomBorderPane.setCenter(labelPathDown);

			labelPathDown.setOnMouseReleased(e -> {
				if (isVisibleLabelPath == true) {
					pathDown = Lib.openFolder();
					if (pathDown != null) {
						bottomPaneFolderPath = new PaneFolderPath(pathDown);
						bottomBorderPane.setCenter(bottomPaneFolderPath);
					} else {
						bottomBorderPane.setCenter(labelPathDown);
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
			if (listImagesLoad != null) {
				/*
				 * TODO first picture should be shown
				 */
				System.out.println("Erstes Bild wird angezeigt");
				nextPicture();

			}

			event.setDropCompleted(listImagesLoad != null);
			event.consume();

		});

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
		System.out.println("Die Größe der Liste ist " + listImagesLoad.size());

		if ((e.getCode().equals(KeyCode.UP) || e.getCode().equals(KeyCode.DOWN)) && fileImageActiv != null) {
			File file = null;
			if (e.getCode().equals(KeyCode.UP)) {
				if (pathUp != null) {
					System.out.println(pathUp.toPath());
					file = new File(pathUp.toPath() + "/" + fileImageActiv.getName());
				}
			} else if (e.getCode().equals(KeyCode.DOWN)) {
				if (pathDown != null) {
					System.out.println(pathDown.toPath());
					file = new File(pathDown.toPath() + "/" + fileImageActiv.getName());
				}
			}
			try {
				if (file != null) {
					Files.move(fileImageActiv.toPath(), file.toPath());
					fileImageActiv = null;
					nextPicture();
				}
			} catch (AccessDeniedException e2) {
				e2.printStackTrace();
				// TODO Meldung auf Oberfläche ausgeben
				System.out.println("Pfad existiert nicht oder wurde noch nicht ausgewählt");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} else if (e.getCode().equals(KeyCode.RIGHT)) {
			nextPicture();
		} else if (e.getCode().equals(KeyCode.LEFT)) {
			lastPicture();
		}

	}

	/**
	 * Sets the counter one step forward, if the counter is smaller than the
	 * size of the Image-List
	 * 
	 * @return boolean - if the stepping forward was successful
	 */

	private void nextPicture() {
		System.out.println(listImagesLoad.size());
		if (listImagesLoad.size() > 0) {
			if (fileImageActiv != null) {
				listImagesLeft.add(0, fileImageActiv);
			}
			fileImageActiv = listImagesLoad.remove(0);
			System.out.println(fileImageActiv);
			myImage = new Image(fileImageActiv.toURI().toString());

			animationImageView.setFromX(root.getWidth());
			setPictureToPane();
		} else {
			activeImageView.setVisible(false);
			borderPane.setCenter(new PaneFinished());
		}
	}

	/**
	 * Sets the counter one step back, if the counter is bigger than Zero.
	 */
	private void lastPicture() {
		if (listImagesLeft.size() > 0) {
			if (fileImageActiv != null) {
				listImagesLoad.add(0, fileImageActiv);
			}
			fileImageActiv = listImagesLeft.remove(0);
			myImage = new Image(fileImageActiv.toURI().toString());
			animationImageView.setFromX(root.getWidth() * (-1));
			setPictureToPane();
		}
	}

	/**
	 * Sets the picture on the imageView
	 * 
	 * @param pictureFile
	 *            - picture that shall be shown
	 */
	public void setPictureToPane() {
		borderPane.getCenter().setVisible(false);
		activeImageView.setVisible(true);
		System.out.println("Index of root: " + root.getChildren().indexOf(borderPane));

		if (fileImageActiv != null) {
			activeImageView.setImage(myImage);
			animationImageView.playFromStart();
		}
	}

	private void moveFile() {
		/*
		 * TODO
		 */
	}

	public void setImageFolder(List<File> imageFolder) {
		listImagesLoad = imageFolder;
		System.out.println(listImagesLoad.size() + " Bilder wurden in die Liste geladen");
	}
}
