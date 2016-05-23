package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private File pathUp;
	private File pathDown;

	private Label labelPathUp = new Label("\uf07c");
	private Label labelPathDown = new Label("\uf07c");
	private Label labelCenter = new Label("\uf03e");

	private List<File> listImages = new ArrayList<File>();

	private AnchorPane centerAnchorPane;

	private ImageView activeImageView;

	private int counter = 0;

	private BorderPane topBorderPane;
	private BorderPane bottomBorderPane;
	private BorderPane centerBorderPane;

	private TranslateTransition animation;

	private StackPane root;
	private Scene scene;

	public void start(Stage primaryStage) {
		try {
			Font.loadFont(getClass().getResource("resources/fontawesome-webfont.ttf").toExternalForm(), 20);
			root = new StackPane();
			BorderPane borderPane = new BorderPane();
			centerAnchorPane = new AnchorPane();
			centerAnchorPane.getStyleClass().add("centerAnchorPane");
			this.initAnchorPangeDragAndDrop(borderPane);
			borderPane.setCenter(centerAnchorPane);

			topBorderPane = new BorderPane();
			topBorderPane.setCenter(labelPathUp);

			topBorderPane.setOnMouseReleased(e -> {
				pathUp = choosePath();
				labelPathUp.setText(pathUp.toURI().toString());
			});

			bottomBorderPane = new BorderPane();
			bottomBorderPane.setCenter(labelPathDown);

			bottomBorderPane.setOnMouseReleased(e -> {
				pathDown = choosePath();
				labelPathDown.setText(pathDown.toURI().toString());
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
			centerAnchorPane.getChildren().add(activeImageView);
			root.getChildren().add(centerAnchorPane);
			root.getChildren().add(borderPane);

			animation = new TranslateTransition(new Duration(500.0), activeImageView);
			animation.setFromX(1200);
			animation.setToX(0);
			// animation.setAutoReverse(true);
			// animation.setCycleCount(Animation.INDEFINITE);
			animation.setInterpolator(Interpolator.LINEAR);

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

	// ################################################## //

	public void initAnchorPangeDragAndDrop(Node node) {

		node.setOnDragOver(event -> {
			Dragboard dragBoard = event.getDragboard();
			if (dragBoard.hasFiles()) {
				for (File file : dragBoard.getFiles()) {
					System.out.println(file.isDirectory());
				}
				event.acceptTransferModes(TransferMode.ANY);
			}
			// event.consume();
		});

		node.setOnDragDropped(event -> {
			Dragboard dragBoard = event.getDragboard();

			setImageFolder(null);

			if (dragBoard.hasFiles()) {
				setImageFolder(dragBoard.getFiles());
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

	// *****************************//

	public void setPictureToPane(File pictureFile) {
		Image myImage = new Image(pictureFile.toURI().toString());
		activeImageView.setImage(myImage);
		animation.playFromStart();

	}

	// *****************************//

	private ImageView createImageView(ReadOnlyDoubleProperty widthProperty) {
		// maintain aspect ratio
		ImageView imageView = new ImageView();
		// set aspect ratio
		imageView.setPreserveRatio(true);
		// resize based on the scnece
		imageView.fitWidthProperty().bind(widthProperty);
		return imageView;
	}

	// *****************************//

	private void handleKeyEvent(KeyEvent e) {

		System.out.println(e.getCode());
		System.out.println(counter);

		if (e.getCode().equals(KeyCode.UP)) {

			boolean worked = nextPicture();

			int counter2 = counter;
			if (worked == true) {
				counter2--;
			}
			System.out.println(listImages.get(counter2).toPath());
			System.out.println(pathUp.toPath());
			File file = new File(pathUp.toPath() + "/" + listImages.get(counter2).getName());
			try {
				Files.move(listImages.get(counter2).toPath(), file.toPath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else if (e.getCode().equals(KeyCode.RIGHT)) {
			nextPicture();
		} else if (e.getCode().equals(KeyCode.LEFT)) {
			lastPicture();

		}

	}

	// *****************************//

	private boolean nextPicture() {
		if (counter < listImages.size() - 1) {
			counter++;
			animation.setFromX(root.getWidth());
			setPictureToPane(listImages.get(counter));
			return true;
		}
		return false;
	}

	private void lastPicture() {
		if (counter > 0) {
			counter--;
			animation.setFromX(root.getWidth() * (-1));
			setPictureToPane(listImages.get(counter));
		}
	}

	private void mooveFile() {
		/*
		 * TODO
		 */
	}

	// *****************************//
	private File choosePath() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose Path");
		File file = chooser.showDialog(new Stage());

		return file;
	}

	public File getSavePath() {
		return pathUp;
	}

	public void setSavePath(File savePath) {
		this.pathUp = savePath;
	}

	public File getDeletePath() {
		return pathDown;
	}

	public void setDeletePath(File deletePath) {
		this.pathDown = deletePath;
	}

	public List<File> getImageFolder() {
		return listImages;
	}

	public void setImageFolder(List<File> imageFolder) {
		this.listImages = imageFolder;
	}
}
