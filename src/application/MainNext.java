package application;

import java.io.File;
import java.util.List;

import application.panes.PaneFolderPath;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainNext extends Application {

	private List<File> listImagesLoad;

	private File pathUp = null;
	private File pathDown = null;

	private Label labelPathUp = new Label("\uf07c");
	private Label labelPathDown = new Label("\uf07c");
	private Label labelCenter = new Label("\uf03e");

	private boolean isVisibleLabelPath = true;

	private BorderPane borderPane;

	private BorderPane topBorderPane;
	private BorderPane bottomBorderPane;
	private BorderPane centerBorderPane;

	private PaneFolderPath topPaneFolderPath;
	private PaneFolderPath bottomPaneFolderPath;

	private FadeTransition animationTopBorderPane;
	private FadeTransition animationBottomBorderPane;

	private ImageScreen is;
	private StackPane root;
	private Scene scene;

	public void start(Stage primaryStage) {
		try {
			Font.loadFont(getClass().getResource("resources/fontawesome-webfont.ttf").toExternalForm(), 20);
			root = new StackPane();
			borderPane = new BorderPane();
			this.initDragAndDrop(root);

			initTopBorderPane();
			initBottomBorderPane();
			initCenterBorderPane();

			root.getChildren().add(borderPane);

			initAnimation();

			root.setOnMouseClicked(e -> {
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
	public void initDragAndDrop(Node node) {
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
				is = new ImageScreen(listImagesLoad, scene);
				root.getChildren().add(is);
			}
			event.setDropCompleted(listImagesLoad != null);
			event.consume();
		});

	}

	/**
	 * Initializing of the handleKeyEvent method of the setOnKeyReleased method
	 */
	private void handleKeyEvent(KeyEvent e) {
		System.out.println(e.getCode());
		System.out.println("Die Größe der Liste ist " + listImagesLoad.size());

		if (e.getCode().equals(KeyCode.UP)) {
			is.up(pathUp);
		} else if (e.getCode().equals(KeyCode.DOWN)) {
			is.down(pathDown);
		} else if (e.getCode().equals(KeyCode.RIGHT)) {
			is.nextImage();
		} else if (e.getCode().equals(KeyCode.LEFT)) {
			is.lastImage();
		}
	}

	private void initTopBorderPane() {
		topBorderPane = new BorderPane();
		topBorderPane.setCenter(labelPathUp);
		labelPathUp.getStyleClass().add("lblPath");

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
		borderPane.setTop(topBorderPane);
	}

	private void initBottomBorderPane() {
		bottomBorderPane = new BorderPane();
		bottomBorderPane.setCenter(labelPathDown);
		labelPathDown.getStyleClass().add("lblPath");

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
		borderPane.setBottom(bottomBorderPane);
	}

	private void initCenterBorderPane() {
		labelCenter.getStyleClass().add("lblCenter");
		centerBorderPane = new BorderPane(labelCenter);
		centerBorderPane.getStyleClass().add("centerBP");
		borderPane.setCenter(centerBorderPane);
	}

	private void initAnimation() {
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
	}

	public void setImageFolder(List<File> imageFolder) {
		listImagesLoad = imageFolder;
		System.out.println(listImagesLoad.size() + " Bilder wurden in die Liste geladen");
	}
}
