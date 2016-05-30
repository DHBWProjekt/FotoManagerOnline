package application.panes;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * The pane that is shown when the last picture in the list is ordered
 * 
 * @author kai
 *
 */
public class PaneFinished extends HBox {
	private Label lblText;
	private Label lblIcon;

	public PaneFinished() {
		super(35);
		lblText = new Label("Fertig");
		lblIcon = new Label("\uf164");

		lblText.getStyleClass().add("lblTextCenter");
		lblIcon.getStyleClass().add("lblIconCenter");

		getStyleClass().add("centerBP");

		getChildren().addAll(lblIcon, lblText);
	}
}
