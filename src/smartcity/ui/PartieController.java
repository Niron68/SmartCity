package smartcity.ui;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import smartcity.Partie;

public class PartieController implements Observer {

	@FXML
	private AnchorPane gameViewParentPane;

	@FXML
	private Label cityTitle;
	@FXML
	private Label teamALabel;
	@FXML
	private Label teamBLabel;
	@FXML
	private Label teamAMoney;
	@FXML
	private Label teamBMoney;
	@FXML
	private Label timeLabel;
	
	private Partie partie;
	
	public PartieController(int tailleMap, String cityName, Equipe configEquipe1, Equipe configEquipe2) {
		this.partie = new Partie(tailleMap, 5.0D, cityName, configEquipe1, configEquipe2);
	}
	
	public PartieController(Partie partie) {
		this.partie = partie;
	}
	
	public void initialize() throws Exception {
		this.cityTitle.setText(this.partie.getCityName());
		this.teamALabel.setText(this.partie.getTeam(0).getTeamName());
		this.teamBLabel.setText(this.partie.getTeam(1).getTeamName());
		this.partie.addObserver(this);

		MapView mapView = new MapView(this.partie);
		this.gameViewParentPane.getChildren().add(mapView);
		
		PartieView partieView = new PartieView(this.partie);
		this.gameViewParentPane.getChildren().add(partieView);

		this.partie.startGame();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.teamAMoney.setText(this.partie.getTeam(0).getMoney() + "€");
		this.teamBMoney.setText(this.partie.getTeam(1).getMoney() + "€");
		
		int elapsedSeconds = (int) this.partie.getElapsedMilliseconds() / 1000;
		int elapsedMinutes = 0;
		while(elapsedSeconds >= 60) {
			elapsedMinutes++;
			elapsedSeconds -= 60;
		}
		this.timeLabel.setText(String.format("%02d", elapsedMinutes) + ":" + String.format("%02d", elapsedSeconds));
	}
	
	@FXML
	public void onQuitButton() {
		Platform.exit();
	}

}