package smartcity.ui;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import smartcity.Partie;
import smartcity.robot.Kind;
import smartcity.robot.Robot;

public class PartieView extends GridPane implements Observer {

	private Image truckB = new Image("file:ressources" + File.separator + "truck_blue.png");
	private Image bikeB = new Image("file:ressources" + File.separator + "moto_blue.png");
	private Image carB = new Image("file:ressources" + File.separator + "car_blue.png");
	private Image droneB = new Image("file:ressources" + File.separator + "drone_blue.png");
	private Image truckR = new Image("file:ressources" + File.separator + "truck_red.png");
	private Image carR = new Image("file:ressources" + File.separator + "car_red.png");
	private Image bikeR = new Image("file:ressources" + File.separator + "moto_red.png");
	private Image droneR = new Image("file:ressources" + File.separator + "drone_red.png");

	private Partie partie;


	public PartieView(Partie partie) {
		this.partie = partie;
		partie.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.getChildren().clear();
		for(Robot rob : this.partie.getTeam(0).getRobotL()){
			ImageView v = new ImageView();
			Image img;
			if(rob.getKind() == Kind.TRUCK) img = this.truckB;
			else if(rob.getKind() == Kind.CAR) img = this.carB;
			else if(rob.getKind() == Kind.BIKE) img = this.bikeB;
			else img = this.droneB;
			v.setImage(img);
			this.add(v, rob.getPosition().getY(), rob.getPosition().getX());
		}
		for(Robot rob : this.partie.getTeam(1).getRobotL()){
			ImageView v = new ImageView();
			Image img;
			if(rob.getKind() == Kind.TRUCK) img = this.truckR;
			else if(rob.getKind() == Kind.CAR) img = this.carR;
			else if(rob.getKind() == Kind.BIKE) img = this.bikeR;
			else img = this.droneR;
			v.setImage(img);
			this.add(v, rob.getPosition().getY(), rob.getPosition().getX());
		}
		this.partie.mapGraphe.preparerGraphe();
		this.partie.mapGraphe.findWay(1, 50);
	}
	
	public int RN(){
		return (int)(Math.floor(Math.random()*26)+1)*16;
	}

}
