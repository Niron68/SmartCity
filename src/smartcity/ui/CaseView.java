package smartcity.ui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CaseView extends Pane {
	
	public CaseView(Color color) {
		super();
		this.setStyle("-fx-background-color: rgb(" + 255 * color.getRed() + ", " + 255 * color.getGreen() + ", " + 255 * color.getBlue() + ")");
		this.setPrefSize(16, 16);
	}

}
