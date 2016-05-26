package application.panes;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PaneFinished extends HBox {
	private Label lblText;
	private Label lblIcon;

	public PaneFinished() {
		super(15);
		lblText = new Label("Fertig");
		lblIcon = new Label("\uf164");

		lblText.getStyleClass().add("lblCenter");
		lblIcon.getStyleClass().add("lblCenter");

		getStyleClass().add("centerBP");

		getChildren().addAll(lblIcon, lblText);
	}
}
