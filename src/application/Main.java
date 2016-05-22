package application;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private URI savePath;
	private URI deletePath;

	private List<File> listImages = new ArrayList<File>();

	private AnchorPane centerAnchorPane;

	private ImageView activeImageView;

	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			centerAnchorPane = new AnchorPane();
			centerAnchorPane.prefWidthProperty().bind(root.widthProperty());
			centerAnchorPane.prefHeightProperty().bind(root.heightProperty());

			centerAnchorPane.getStyleClass().add("centerAnchorPane");
			this.initAnchorPangeDragAndDrop(centerAnchorPane);
			root.setCenter(centerAnchorPane);
			activeImageView = createImageView(centerAnchorPane.widthProperty());
			centerAnchorPane.getChildren().add(activeImageView);

			Scene scene = new Scene(root, 400, 400);
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
			if (dragBoard.hasFiles() || dragBoard.hasUrl()) {
				event.acceptTransferModes(TransferMode.ANY);
			}
			event.consume();
		});

		node.setOnDragDropped(event -> {
			Dragboard dragBoard = event.getDragboard();

			String url = null;
			setImageFolder(null);

			if (dragBoard.hasFiles()) {
				setImageFolder(dragBoard.getFiles());
				System.out.println("List of files incoming");
			}
			if (getImageFolder() != null) {
				/*
				 * TODO first picture should be shown
				 */

				setPictureToPane(listImages.get(0));

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

	public URI getSavePath() {
		return savePath;
	}

	public void setSavePath(URI savePath) {
		this.savePath = savePath;
	}

	public URI getDeletePath() {
		return deletePath;
	}

	public void setDeletePath(URI deletePath) {
		this.deletePath = deletePath;
	}

	public List<File> getImageFolder() {
		return listImages;
	}

	public void setImageFolder(List<File> imageFolder) {
		this.listImages = imageFolder;
	}
}
