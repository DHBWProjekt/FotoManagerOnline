package application.panes;

import java.io.File;

import application.Lib;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PaneFolderPath extends HBox {

	private Label lblText;
	private Label lblIcon;

	private File folder;

	public PaneFolderPath(File folder) {
		super(15);
		this.folder = folder;
		lblText = new Label(folder.getName());
		lblIcon = new Label("\uf08e");

		lblText.getStyleClass().add("lblFolderPath");
		lblIcon.getStyleClass().add("lblIcon");

		getStyleClass().add("pnlFolderPath");

		getChildren().addAll(lblText, lblIcon);
		lblIcon.setOnMouseClicked(e -> {
			Lib.openExplorer(folder);
			e.consume();
		});
	}

	public File getFolder() {
		return folder;
	}
}
